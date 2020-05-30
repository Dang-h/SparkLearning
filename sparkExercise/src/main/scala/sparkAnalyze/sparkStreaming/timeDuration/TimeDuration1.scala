package sparkAnalyze.sparkStreaming.timeDuration

import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

object TimeDuration1 {
	def main(args: Array[String]): Unit = {
		val sc = new SparkContext(new SparkConf().setMaster("local[*]").setAppName("Time Duration1"))
		val ssc = new StreamingContext(sc, Seconds(1))

		val inputData: ReceiverInputDStream[String] = ssc.socketTextStream("hadoop100", 9999)

		val result: DStream[(String, Int)] = inputData.map(data => (data+"_Done", 1))
		  .reduceByKeyAndWindow((_: Int) + (_: Int), Seconds(3), Seconds(1))

		result.print()

		ssc.start()
		ssc.awaitTermination()
	}

}
