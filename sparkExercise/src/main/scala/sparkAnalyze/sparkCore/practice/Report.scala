package sparkAnalyze.sparkCore.practice

import org.apache.spark.rdd.RDD
import org.apache.spark.util.LongAccumulator
import org.apache.spark.{SparkConf, SparkContext}

/*
成绩单（data/practice/data.txt）格式：
姓名、课程、成绩
求：
求每名同学选修的课程数
求各门课程的平均分
使用累加器求DataBase课程的选修人数
 */
object Report {
	def main(args: Array[String]): Unit = {
		val sc = new SparkContext(new SparkConf().setMaster("local[*]").setMaster("Report"))

		val lines: RDD[String] = sc.textFile("data/practice/data.txt")

		println("--------------------------求每名同学选修的课程数----------------------------------")
		val par: RDD[(String, (String, Int))] = lines
		  .map(row =>
			  (row.split(",")(0), // 获取学生姓名
				row.split(",")(1)) // 获取课程名称
		  ).mapValues(x => (x, 1)) // 给课程加上标签1 --> (课程名, 1)

		// reduceByKey中按照函数f，对相同的Key的Value进行聚合
		// 此处的函数f为(x: (String, Int), y: (String, Int)) => (" ", x._2 + y._2)
		// x和y分别为第一个元素的value和第二个元素的value。(课程名，1)
		val value: RDD[(String, (String, Int))] = par.reduceByKey((x, y) => (" ", x._2 + y._2))

		println("--------------------------求各门课程的平均分----------------------------------")
		// (课程名，分数)
		val pare: RDD[(String, Int)] = lines.map(row => (row.split(",")(1), row.split(",")(2).toInt))

		// (课程名，（分数，1）)
		val value1: RDD[(String, (Int, Int))] = pare.mapValues(x => (x, 1))

		// (课程名，（该课程总分，该课程选修总数）)
		val value2: RDD[(String, (Int, Int))] = value1.reduceByKey((x, y) => (x._1 + y._1, x._2 + y._2))

		// (课程名，平均分)
		val value3: RDD[(String, Int)] = value2.mapValues(x => (x._1 / x._2))

		println("--------------------------求DataBase课程的选修人数----------------------------------")
		val value4: RDD[(String, Int)] = lines
		  .filter(row => row.split(",")(1) == "DataBase") // 筛选出选秀DataBase的数据
		  .map(row => (row.split(",")(1), 1)) // (课程名， 1)

		val acc: LongAccumulator = sc.longAccumulator("SumDataBaseAcc")

		value4.values.foreach(x => acc.add(x))

		acc.value

		sc.stop()
	}

}
