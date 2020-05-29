package sparkAnalyze.sparkStreaming.readFile

import kafka.api.{OffsetRequest, PartitionOffsetRequestInfo, TopicMetadata, TopicMetadataRequest, TopicMetadataResponse}
import kafka.common.TopicAndPartition
import kafka.consumer.SimpleConsumer
import kafka.message.MessageAndMetadata
import kafka.serializer.StringDecoder
import kafka.utils.{ZKGroupTopicDirs, ZkUtils}
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka.{HasOffsetRanges, KafkaUtils, OffsetRange}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable
import scala.util.{Success, Try}

object ReadKafka1 {
	def main(args: Array[String]): Unit = {
		val sc = new SparkContext(new SparkConf().setMaster("local[*]").setAppName("Read Kafka data with low API"))

		val ssc = new StreamingContext(sc, Seconds(3))

		// 配置kafka需要的相关参数
		val topics = Set("spark_streaming_test")
		val kafkaParams = mutable.Map[String, String]()
		kafkaParams.put("bootstrap.servers", "hadoop100:9092") // kafka集群，broker-list
		kafkaParams.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
		kafkaParams.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
		kafkaParams.put("session.timeout.ms", "30000")
		kafkaParams.put("enable.auto.commit", "false") // 是否自动提交偏移量
		kafkaParams.put("max.poll.records", "100")
		kafkaParams.put("kafka.topics", "spark_streaming_test")
		kafkaParams.put("group.id", "spark_test")

		// 配置zookeeper相关参数
		val zkHost = "hadoop100:2181,hadoop101:2181,hadoop102:2181"
		val sessionTimeout = 120000
		val connectionTimeout = 60000
		val zkClient = ZkUtil.initZKClient(zkHost, sessionTimeout, connectionTimeout)

		/* 用于zookeeper相关操作
		   在zookeeper中，消费的偏移量信息存储在zookeeper内对应的目录中，每个分区一个文件
		   如：
		   	 消费组名：spark_test
		   	 消费主题：spark_streaming_test
		   	 分区数：3
		   	 则3个偏移量信息在zookeeper内的存储位置为：/consumer/spark_test/offsets/spark_streaming_test，目录下
		   	 文件名分别为：0，1，2；在kafka中每个分区的偏移量时相互隔离的
		 */
		val zkTopic = "spark_streaming_test" //Topic名称
		val zkConsumerGroupId = "spark_test" // 消费组名称

		// 得到偏移量信息存储目录
		val zkTopicDir = new ZKGroupTopicDirs(zkConsumerGroupId, zkTopic)
		val zkTopicPath: String = zkTopicDir.consumerOffsetDir

		// 得到偏移量信息的存储目录中的文件个数（kafka多少个分区，多少个文件）
		val childrenCount: Int = zkClient.countChildren(zkTopicPath)

		// 将kafka中的数据流转换成DStram
		var kafkaStream: InputDStream[(String, String)] = null
		// Key为主题与分区信息，value为偏移量信息
		var fromOffsets: Map[TopicAndPartition, Long] = Map()

		println("--------------------手动读取偏移量并创建InputDStream------------------------------")
		kafkaStream = if (childrenCount > 0) {
			// 通过向kafka发送一个TopicMetadataRequest实例，以获得一个TopicMetadataResponse实例
			// 便可以获取当前kafka指定主题中各个分区的分布状态

			val req = new TopicMetadataRequest(topics.toList, 0)
			val leaderConsumer = new SimpleConsumer("hadoop100", 9092, 10000, 10000, "StreamingOffsetObserver")

			val res: TopicMetadataResponse = leaderConsumer.send(req)
			val topicMetaOption: Option[TopicMetadata] = res.topicsMetadata.headOption

			val partitions: Map[Int, String] = topicMetaOption match {
				case Some(tm) => tm.partitionsMetadata.map(
					pm => (pm.partitionId, pm.leader.get.host)).toMap[Int, String]
				case None => Map[Int, String]()
			}

			// 读取zookeeper中每个分区的偏移量信息，并保存在Map[TopicAndPartition, Long]集合的fromOffsets中
			for (partition <- 0 until childrenCount) {

				val partitionOffset = zkClient.readData[String](zkTopicPath + "/" + partition)
				val tp = TopicAndPartition(kafkaParams("kafka.topics"), partition)
				val requestMin = OffsetRequest(Map(tp -> PartitionOffsetRequestInfo(OffsetRequest.EarliestTime, 1)))
				val consumerMin = new SimpleConsumer(partitions(partition), 9092, 10000, 10000, "getMinOffset")
				val curOffset: Seq[Long] = consumerMin.getOffsetsBefore(requestMin).partitionErrorAndOffsets(tp).offsets
				var nextOffset: Long = partitionOffset.toLong

				if (curOffset.nonEmpty && nextOffset < curOffset.head) {
					nextOffset = curOffset.head
				}

				fromOffsets += (tp -> nextOffset)
			}

			// 通过泛型分别设置从Kafka中读取消息的Key和Value类型均为String，
			// 并设置Key和Value的解码器均为StringDecoder
			// 泛型（String， String） 指定读取的数据以什么格式返回
			// 最后在方法中传入对应参数
			val messageHandler = (mam: MessageAndMetadata[String, String]) => (mam.key, mam.message)
			// 得到InputDStream[(消息的Key，消息的Value)]
			KafkaUtils.createDirectStream[
			  String,
			  String,
			  StringDecoder,
			  StringDecoder,
			  (String, String)](ssc, kafkaParams.toMap, fromOffsets, messageHandler)
		} else { // 直接通过KafkaUtils.createDirectStream方法创建DStream，意味着从头开始消费Kafka消息
			KafkaUtils.createDirectStream[
			  String,
			  String,
			  StringDecoder,
			  StringDecoder](ssc, kafkaParams.toMap, topics)
		}

		println("----------------------------分析数据并更新保存偏移量----------------------------------------")

		var offsetRanges: Array[OffsetRange] = null // 用于存放当前读取的这批数据所对应的偏移量信息

		val kafkaInputDStream: DStream[(String, String)] = kafkaStream.transform(
			(rdd: RDD[(String, String)]) => {
				offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
				rdd
			}
		)

		val kafkaValues: DStream[String] = kafkaInputDStream.map(_._2)
		val kafkaSplits: DStream[Array[String]] = kafkaValues.map(_.split(","))
		val kafkaFilters: DStream[Array[String]] = kafkaSplits.filter(arr => {
			if (arr.length == 3) {
				Try(arr(2).toInt) match {
					case Success(_) if arr(2).toInt > 3 => true
					case _ => false
				}
			} else {
				false
			}
		})

		val results: DStream[String] = kafkaFilters.map(_.mkString(","))
		results.foreachRDD(rdd => {
			// 在Driver进程中执行
			rdd.foreachPartition(p => p.foreach(result => println(result)))

			// 消费成功更新保存偏移量到Zookeeper
			for (elem <- offsetRanges) {
				ZkUtils.updatePersistentPath(zkClient,
					zkTopicDir.consumerOffsetDir + "/" + elem.partition,
					elem.fromOffset.toString)
				println("本次消费成功后，偏移量状态： " + elem)
			}
		})

		ssc.start()
		ssc.awaitTermination()
	}
}
