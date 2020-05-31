package sparkAnalyze.sparkStreaming

import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}

object WordCount1 {
	def main(args: Array[String]): Unit = {
		val sc = new SparkContext(new SparkConf().setMaster("local[*]").setAppName("wordCount by updateStateByKey"))
		val ssc = new StreamingContext(sc, Seconds(5))
		// 指定checkpoint存储路径
		ssc.checkpoint("data/tmp/sparkStreaming")

		val lineDStream: ReceiverInputDStream[String] = ssc.socketTextStream("hadoop100", 9999)

		/**
		 * 将上一个批次的结果与当前批次合并
		 *
		 * @param iter
		 * -String：当前单词;
		 * -Seq[Int]:当前批次中单词出现的个数；
		 * -Option[Int]:当前单词在上一个批次中出现的个数
		 * @return
		 */
		def updateStateFunc(iter: Iterator[(String, Seq[Int], Option[Int])]): Iterator[(String, Int)] = {
			iter.map {
				case (word, curWordCount, preWordCount) => {
					(word, curWordCount.sum + preWordCount.getOrElse(0))
				}
			}
		}

		// 偏应用函数
		val func: Iterator[(String, Seq[Int], Option[Int])] => Iterator[(String, Int)] = updateStateFunc

		val wordMap: DStream[(String, Int)] = lineDStream.flatMap(_.split(" ")).map(x => (x, 1))

		// 通过将Key原始状态与新状态通过f中定义的方式进行更新。可用于对某个统计变量进行`全局持续的累加`。
		val resultStateDStream: DStream[(String, Int)] = wordMap.updateStateByKey[Int](
			func, // 自定义函数
			new HashPartitioner(sc.defaultParallelism), // 指定分区器
			true // 是否将该分区器应用在后续生成的RDD中
		)

		resultStateDStream.print()

		ssc.start()
		ssc.awaitTermination()
	}

}
