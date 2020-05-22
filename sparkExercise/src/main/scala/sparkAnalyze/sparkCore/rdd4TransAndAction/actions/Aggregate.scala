package sparkAnalyze.sparkCore.rdd4TransAndAction.actions

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Aggregate {
	def main(args: Array[String]): Unit = {
		val conf = new SparkConf()
		  .setMaster("local[*]")
		  .setAppName("Chapter5_2_1_7")
		val sc = new SparkContext(conf)

		import collection.mutable.ListBuffer
		val rddData1: RDD[(String, String)] = sc.parallelize(
			Array(
				("用户1", "接口1"),
				("用户2", "接口1"),
				("用户1", "接口1"),
				("用户1", "接口2"),
				("用户2", "接口3")),
			2)

		// 初始值类型为ListBuffer[String]
		// seqOp函数，将RDD中每个元素的”接口“取出，并聚合到zeroValue中
		// combOp函数，对每个分区聚合的临时结果做最终聚合
		val result: ListBuffer[String] = rddData1.aggregate(ListBuffer[String]())( // 初始值是ListBuffer
			(list: ListBuffer[String], tuple: (String, String)) => list += tuple._2,
			// tuple为(”用户“，”接口“),将各分区中的”接口“放到ListBuffer中
			(list1: ListBuffer[String], list2: ListBuffer[String]) => list1 ++= list2)
			// 将每个分区临时聚合的数据合并

		println(result)
		sc.stop()
	}
}
