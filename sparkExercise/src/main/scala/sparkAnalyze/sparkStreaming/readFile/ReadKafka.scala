package sparkAnalyze.sparkStreaming.readFile

import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

import scala.util.{Success, Try}

/*
使用kafka高阶API中处理数据（自动维护偏移量）
数据格式为：进站口名称、出站口名称、消费金额（元）
StationA, StationB, 2
输出消费金额大于3元的用户
 */
object ReadKafka {
	def main(args: Array[String]): Unit = {
		val sc = new SparkContext(new SparkConf().setMaster("local[*]").setAppName("ReadKafka"))
		val ssc = new StreamingContext(sc, Seconds(10))

		val kafkaInputDStream: ReceiverInputDStream[(String, String)] = KafkaUtils.createStream(
			ssc, // StreamingContext
			"hadoop100:2181,hadoop101:2181,hadoop102:2181", // zookeeper地址
			"spark_test", // 设置sparkStreaming作为Kafka消费者所在的消费组组名
			Map("spark_streaming_test" -> 2) // 设置消费者Topic和每个分区线程数
		)

		val kafkaValues: DStream[String] = kafkaInputDStream.map(_._2)

		val kafkaSplit: DStream[Array[String]] = kafkaValues.map(_.split(","))

		val kafkaFilter: DStream[Array[String]] = kafkaSplit.filter(arr => {
			if (arr.length == 3) {
				Try(arr(2).toInt) match {
					case Success(_) if arr(2).toInt > 3 => true
					case _ => false
				}
			} else {
				false
			}
		})

		val result: DStream[String] = kafkaFilter.map(_.mkString(","))

		result.foreachRDD(rdd =>{
			// 在Driver上执行
			rdd.foreachPartition(p => {
				// 在Executor上执行
				p.foreach(result => println(result))
			})
		})

		ssc.start()
		ssc.awaitTermination()
	}

}
