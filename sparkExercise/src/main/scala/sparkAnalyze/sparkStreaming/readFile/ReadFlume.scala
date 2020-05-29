package sparkAnalyze.sparkStreaming.readFile

import java.net.InetSocketAddress

import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.dstream.ReceiverInputDStream
import org.apache.spark.streaming.flume.{FlumeUtils, SparkFlumeEvent}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

/*
Flume和SparkStreaming对接需要在Flume/lib下加入相关jar包（spark-streaming-flume_2.11-2.3.1.jar）
替换Flume/lib下的scala的jar包和自己使用的版本一致（scala-library-2.10.5.jar-->scala-library-2.11.12.jar)
 */
object ReadFlume {
	def main(args: Array[String]): Unit = {
		val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("Read Flume Data")
		val sc = new SparkContext(conf)
		val ssc = new StreamingContext(sc, Seconds(3))

		// 端口未知，卒
		val flumeAddress = Seq(new InetSocketAddress("hadoop100", 44444))
		val flumeEventDStream: ReceiverInputDStream[SparkFlumeEvent] = FlumeUtils.createPollingStream(
			ssc,
			flumeAddress,
			StorageLevel.MEMORY_AND_DISK_SER_2)

		val flumeDStream = flumeEventDStream.map(s => new String(s.event.getBody.array()))
		val uidDStream = flumeDStream.map(u => (u.split(",")(0), 1))
		val uidCount = uidDStream.reduceByKey(_ + _)
		uidCount.print()

		ssc.start()
		ssc.awaitTermination()
	}
}
