package sparkAnalyze.sparkCore.rdd4Advance

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Cache {
	def main(args: Array[String]): Unit = {
		val conf = new SparkConf()
		  .setMaster("local[*]")
		  .setAppName("CacheTest")
		val sc = new SparkContext(conf)

		// 数据格式：访问时间， 用户id， 查询词， 该URL在返回结果中的排名， 用户点击的顺序号， 用户点击的URL
		val sogouRDD = sc.textFile("data/practice/SogouQ1.txt", 8)
//		val rddData1 = sogouRDD.map(t => (t.split("\t")(1), 1)) //(user_ID, 1)
		val rddData1 = sogouRDD.map(t => (t.split("\t")(5), 1)) //(URL, 1)
//		val rddURL = rddData1.map(c => (c._1.split("((?<=\\/\\/www\\.)[^\\/\\:]+|(?<=\\/\\/)(?!www\\.)[^\\/\\:]+)")(1), 1))
		// split(rex, limit)中limit为限制截取数组长度，split(rex)(num) 其中num为split截取出来的数组下标
		rddData1.cache
		val rddData2 = rddData1.reduceByKey(_ + _)

		val rddData3 = rddData2.sortBy(_._2, ascending = false)

		println(rddData3.count)
		println(rddData3.take(3))
		println(rddData1.count)
		println(rddData1.take(3))

		sc.stop()

	}
}
