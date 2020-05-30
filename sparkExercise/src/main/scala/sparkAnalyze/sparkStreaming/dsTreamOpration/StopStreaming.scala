package sparkAnalyze.sparkStreaming.dsTreamOpration


import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.streaming.dstream.ReceiverInputDStream
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object StopStreaming {
	// 稍后用于存放操作HDFS中的文件
	var hadoopConf: Configuration = _
	val shutdownMarkerPath = new Path("hdfs://hadoop100:9000/user/test/spark_shutdown_marker")
	// 指定目录不存在则为false
	var stopMarker: Boolean = false

	/**
	 * 用于判断HDFS中指定目录是否存在
	 */
	def checkShutdownMarker = {
		if (!stopMarker) {
			val fs = FileSystem.get(hadoopConf)
			stopMarker = fs.exists(shutdownMarkerPath)
		}
	}

	def main(args: Array[String]): Unit = {
		val sc = new SparkContext(new SparkConf().setMaster("local[*]").setAppName("StopStreaming"))
		val ssc = new StreamingContext(sc, Seconds(5))

		hadoopConf = ssc.sparkContext.hadoopConfiguration
		hadoopConf.set("fs.defaultFS", "hdfs://hadoop100:9000")

		val lineDStream: ReceiverInputDStream[String] = ssc.socketTextStream("hadoop100", 9999)
		lineDStream.flatMap(_.split(" ")).map(x => (x, 1)).reduceByKey(_ + _).print()

		ssc.start()
		val checkIntervalMills = 2 * 1000 * 1
		var isStopped = false

		while (!isStopped) {
			println("正在确认关闭状态")
			isStopped = ssc.awaitTerminationOrTimeout(checkIntervalMills)

			if (isStopped) {
				println("SparkStreaming已经关闭")
			} else {
				println("SparkStreaming 运行中...")
			}

			checkShutdownMarker
			if (!isStopped && stopMarker) {
				println("准备关闭SparkStreaming")
				ssc.stop(true,true)
			}
		}
	}
}
