package sparkAnalyze.sparkCore.rdd4TransAndAction.transformations.keyValueType

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * def combineByKey[C](
 * createCombiner: V => C,	//遍历RDD中每一个元素，若该元素K第一次被访问，则调用，将元素从V类型转C类型（类型可相同）
 * mergeValue: (C, V) => C, //将C类型的值与V类型的值聚合为C类型
 * mergeCombiners: (C, C) => C): RDD[(K, C)] //多分区聚合
 */
object CombineByKey {
	def main(args: Array[String]): Unit = {
		val conf = new SparkConf()
		  .setMaster("local[*]")
		  .setAppName("Chapter5_1_2_4")
		val sc = new SparkContext(conf)

		val rddData1 = sc.parallelize(
			Array(
				("班级1", 95f),
				("班级2", 80f),
				("班级1", 75f),
				("班级3", 97f),
				("班级2", 88f)),
			2)

		val rddData2 = rddData1.combineByKey(
			// 将分数映射成（分数，人数），一个分数对应一位学生，则（分数，1）
			grade => (grade, 1),
			// 将相同班级内所有学生分数求和，人数求和，返回（总分数、总人数）
			(gc: (Float, Int), grade) => (gc._1 + grade, gc._2 + 1),
			// 多分区合并，返回（最终分数，最终人数）
			(gc1: (Float, Int), gc2: (Float, Int)) => (gc1._1 + gc2._1, gc1._2 + gc2._2))

		val rddData3 = rddData2.map(t => (t._1, t._2._1 / t._2._2))

		println(rddData3.collect.mkString(","))

		println("--------------------------------------------------------------------------------")
		val input: RDD[(String, Int)] = sc.parallelize(Seq(
			("t1", 1), ("t1", 2), ("t1", 3), ("t2", 2), ("t2", 5)))
		val total: RDD[(String, (Int, Int))] = input.combineByKey(
			v => (v, 1),
			(acc: (Int, Int), v) => (acc._1 + v, acc._2 + 1),
			(acc1: (Int, Int), acc2: (Int, Int)) => (acc1._1 + acc2._1, acc2._2 + acc2._2)
		)
		// 求平均值
		val result: RDD[(String, Float)] = total.map { case (k, v) => (k, v._1 / v._2.toFloat) }

		result.collectAsMap().foreach(println)

		sc.stop()
	}
}
