package sparkAnalyze.sparkCore.practice

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object ClickTop3 {
	/*
	数据结构：时间戳，省份，城市，用户，广告，中间字段使用空格分割。
			1516609143867 6 7 64 16
			1516609143869 9 4 75 18
			1516609143869 1 7 87 12
	需求：
		统计出每一个省份广告被点击次数的 TOP3
		每个省份，该省份广告点击次数的Top3：按省份划分，对给省份的广告做count
	 */
	def main(args: Array[String]): Unit = {
		// 1 初始化spark配置信息并建立与spark的连接
		val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("AdClickTop3")
		val sc = new SparkContext(conf)

		// 2 读取数据生成RDD：TS, province, City, User, Ad
		val inputDate: RDD[String] = sc.textFile("data/practice/agent.log")

		// 3 对数据进心切分，按照最小粒度聚合，((Province,Ad),1)
		val provinceAd2One = inputDate.map { line =>
			val fields: Array[String] = line.split(" ")
			((fields(1), fields(4)), 1)
		}

		// 4 计算每个省份中每个广告的点击总数((Province, Ad),sum)
		val provinceAd2Sum = provinceAd2One.reduceByKey(_ + _)

		// 5 转换数据形式，Province作为Key，Ad和sum作为Value，(Province, (Ad, sum))
	  val province2AdSum: RDD[(String, (String, Int))] = provinceAd2Sum.map(x => (x._1._1, (x._1._2, x._2)))

		// 6 将同一个省份的所有广告进行分组聚合，(Province, List((Ad1, sum1), (Ad2, sum2),...))
		val provinceGroup: RDD[(String, Iterable[(String, Int)])] = province2AdSum.groupByKey()

		// 7 对同一省份所有广告的集合进行排序取前3，排序规则为广告的点击数
		val provinceAdTop3: RDD[(String, List[(String, Int)])] = provinceGroup.mapValues(x => x.toList.sortWith((item1, item2) => item1._2 > item2._2).take(3))

		// 8 数据拉取到Driver端，打印
		// (Province, List((Ad,ClickCount)))
		provinceAdTop3.collect().foreach(println)

		// 9 关闭与spark的连接
		sc.stop()
	}
}
