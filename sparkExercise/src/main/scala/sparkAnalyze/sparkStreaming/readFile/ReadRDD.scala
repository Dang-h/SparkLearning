package sparkAnalyze.sparkStreaming.readFile

import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.collection.mutable

object ReadRDD {
	def main(args: Array[String]): Unit = {
		val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("Read RDD Files")
		val sc = new SparkContext(conf)
		sc.setLogLevel("INFO")
		val ssc = new StreamingContext(sc, Seconds(1))

		// 创建一个线程安全的RDD数据队列
		val rddQueue = new mutable.SynchronizedQueue[RDD[Int]]

		// 创建一个子线程，通过for像队列中持续添加RDD
		// 每次添加成功后子线程睡眠2s
		val addQueueThread = new Thread(new Runnable {
			override def run(): Unit = {
				for (i <- 5 to 10) {
					rddQueue += sc.parallelize(1 to i, 1)
					Thread.sleep(2000)
				}
			}
		})

		val inputDStream: InputDStream[Int] = ssc.queueStream(rddQueue)
		inputDStream.print()

		ssc.start()
		addQueueThread.start()

		ssc.awaitTermination()
	}
}
