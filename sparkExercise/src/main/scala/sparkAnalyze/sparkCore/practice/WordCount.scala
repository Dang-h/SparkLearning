package sparkAnalyze.sparkCore.practice

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object WordCount {
	def main(args: Array[String]): Unit = {
		val sc = new SparkContext(new SparkConf().setMaster("local[*]").setAppName("WordCount1"))

		val inputData: RDD[String] = sc.textFile("data/words.txt")

		val mapRDD: RDD[Array[String]] = inputData.map(line => line.trim.split("\\s+"))

		val flatMapRDD: RDD[String] = mapRDD.flatMap(word => word)

		val mapRDD1: RDD[(String, Int)] = flatMapRDD.map(words => (words, 1))

		val groupRDD: RDD[(String, Iterable[Int])] = mapRDD1.groupByKey()

		val result: RDD[(String, Int)] = groupRDD.map(words => {
			val list: List[Int] = words._2.toList
			(words._1, list.sum)
		})

		result.collect.foreach(println)

		sc.stop()
	}

}
