package sparkAnalyze.sparkStreaming

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object WordCount {
	def main(args: Array[String]): Unit = {

		val ssc = new StreamingContext(new SparkContext(new SparkConf().setMaster("local[4]").setAppName("StreamingWordCount")), Seconds(5))

		// 通过监听netcat窗口创建DStream，读取命令窗口一行行数据
		val lineStream = ssc.socketTextStream("hadoop100", 9999)

		// 进行wordCount
		val words: DStream[(String, Int)] = lineStream.flatMap(line => line.split(" ")).map((_, 1))

		words.reduceByKey(_ + _).print()

		// 启动采集器
		ssc.start()

		// Driver 等待采集器执行完毕
		ssc.awaitTermination()
	}
}
