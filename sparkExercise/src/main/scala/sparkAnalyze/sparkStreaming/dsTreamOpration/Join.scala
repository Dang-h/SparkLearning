package sparkAnalyze.sparkStreaming.dsTreamOpration

import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/*
将两个DStream(K,V)进行join，并返回一个新的DStream(K,(V,w))
 */
object Join {
	/*
	SparkStreaming 从9999端口读取学生姓名和年龄，从9998端口读取学生姓名和考试成绩
	输出成绩≥85的学生信息
	 */
	def main(args: Array[String]): Unit = {
		val sc = new SparkContext(new SparkConf().setMaster("local[*]").setAppName("Join"))
		val ssc = new StreamingContext(sc, Seconds(40))

		val line1Stream: ReceiverInputDStream[String] = ssc.socketTextStream("hadoop100", 9999)
		val line2Stream: ReceiverInputDStream[String] = ssc.socketTextStream("hadoop100", 9998)

		val kvDStream1: DStream[(String, String)] = line1Stream.map(_.split(","))
		  .filter(_.length == 2).map(arr => (arr(0), arr(1)))
		val kvDStream2 = line2Stream.map(_.split(","))
		  .filter(arr => arr.length == 2 && arr(1).toInt >= 85).map(arr => (arr(0), arr(1)))

		val joinDStream: DStream[(String, (String, String))] = kvDStream1.join(kvDStream2)
		joinDStream.print()

		ssc.start()
		ssc.awaitTermination()
	}
}
