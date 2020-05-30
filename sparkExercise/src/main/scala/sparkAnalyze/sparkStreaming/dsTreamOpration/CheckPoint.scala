package sparkAnalyze.sparkStreaming.dsTreamOpration

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/*
利用checkpoint使程序重启时可以恢复至上一次运行时的状态
 */
object CheckPoint {
	val checkpointPath = "data/tmp/checkpointDir"

	/**
	 * createContext方法，实例化SparkConf、StreamingContext并设置Checkpoint目录
	 *
	 * @param host          netCat服务器地址
	 * @param port          netCat端口
	 * @param checkpointDir checkpoint目录
	 * @return
	 */
	def createContext(host: String, port: Int, checkpointDir: String): StreamingContext = {
		println("创建新的Context")
		val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("CheckpointTest")
		val ssc = new StreamingContext(conf, Seconds(5))
		ssc.checkpoint(checkpointDir)

		val lineDStream: ReceiverInputDStream[String] = ssc.socketTextStream(host, port)
		val wordMap: DStream[(String, Int)] = lineDStream.flatMap(_.split(" ")).map(x => (x, 1))

		// 将DStream中的RDD按指定时间间隔做checkpoint
		wordMap.checkpoint(Seconds(5 * 5))

		val wordCounts: DStream[(String, Int)] = wordMap.reduceByKey(_ + _)
		wordCounts.print()

		ssc
	}

	def main(args: Array[String]): Unit = {

		// 通过StreamingContext.getOrCreate方法从checkpoint目录中恢复StreamingContext状态
		val ssc: StreamingContext = StreamingContext.getOrCreate(
			checkpointPath,
			// createContext称为方法，不可以直接作为参数进行传递
			// （）=>createContext称为函数，可以作为参数直接进行传递
			() => createContext("hadoop100", 9999, checkpointPath)
		)

		ssc.start()
		ssc.awaitTermination()
	}
}
