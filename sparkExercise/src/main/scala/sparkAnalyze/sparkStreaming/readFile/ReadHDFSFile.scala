package sparkAnalyze.sparkStreaming.readFile

import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object ReadHDFSFile {
	def main(args: Array[String]): Unit = {
		val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("Read HDFS Files")
		val sc = new SparkContext(conf)
		val ssc = new StreamingContext(sc, Seconds(3))

		// 读取HDFS文件夹
		val lines: DStream[String] = ssc.textFileStream("hdfs://hadoop100:9000/user/test/DStream")

		lines.print()
		ssc.start()

		ssc.awaitTermination()


	}

}
