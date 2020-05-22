package sparkAnalyze.sparkCore.rdd4TransAndAction.transformations.valueType

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object GroupBy {
	def main(args: Array[String]): Unit = {
		val conf = new SparkConf()
		  .setMaster("local[*]")
		  .setAppName("GroupBy")
		val sc = new SparkContext(conf)

		val rdd: RDD[Int] = sc.parallelize(1 to 4)

		val groupByRDD: RDD[(Int, Iterable[Int])] = rdd.groupBy(_ % 2)

		groupByRDD.collect().foreach(println)
	}

}
