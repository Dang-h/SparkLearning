package sparkAnalyze.sparkStreaming.readFile

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


	}
}
