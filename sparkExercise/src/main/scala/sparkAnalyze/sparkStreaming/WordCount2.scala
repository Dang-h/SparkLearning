package sparkAnalyze.sparkStreaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

object WordCount2 {
	val checkpointDir = "data/tmp/wordCountCheckpointDir"

	def createContext(host: String, port: Int, checkpointDir: String): StreamingContext = {
		println("创建新的Context")

		val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("WordCount with customAccumulator")
		val ssc = new StreamingContext(conf, Seconds(5))
		ssc.checkpoint(checkpointDir)

		val wordCounts: DStream[(String, Int)] = ssc.socketTextStream(host, port)
		  .flatMap(_.split(" "))
		  .map(x => (x, 1))
		  .reduceByKey(_ + _)

		wordCounts.checkpoint(Seconds(5 * 10))

		// foreachRDD中的f在Driver中执行
		wordCounts.foreachRDD(rdd => {
			val wordCountAcc: WordCountAccumulator[(String, Int)] = WordCountAccumulator.getInstance(rdd.sparkContext)
			// foreachPartition在Executor中执行；foreach在Executor中执行
			rdd.foreachPartition(p => p.foreach(t => wordCountAcc.add(t)))

			wordCountAcc.value.foreach(println)
		})

		ssc
	}

	def main(args: Array[String]): Unit = {

		val ssc: StreamingContext = StreamingContext.getOrCreate(
			checkpointDir,
			() => createContext("hadoop100", 9999, checkpointDir)
		)

		ssc.start()
		ssc.awaitTermination()
	}
}
