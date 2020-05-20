package sparkAnalyze.sparkCore.noteThat

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object RDDSerialization {
	def main(args: Array[String]): Unit = {
		val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("wordCount_RDDSer")
		val sc = new SparkContext(conf)

		val inputRdd: RDD[String] = sc.parallelize(Array("hadoop", "spark", "hive", "Serialization"))
		val search = new Search("hadoop")


		val match1: RDD[String] = search.getMatch1(inputRdd)
		match1.collect().foreach(println)

		sc.stop()
	}
}

class Search(query:String) extends Serializable {
	// 过滤出包含字符串的数据
	def isMatch(s: String) = {
		s.contains(s)
	}

	// 过滤出包含字符串的RDD
	def getMatch1(rdd: RDD[String]) = {
		// 调用isMatch，实际上调用的是this.isMatch()其中this表示Search这个类的对象，
		// 程序运行过程中需要将Search队形序列化传递到Executor端
		rdd.filter(isMatch)
	}

	def getMatch2(rdd: RDD[String]) = {
		// 调用的query是定义在Search类中的字段，调用的是this.query，同样需要序列化
		// 将变量赋值给局部变量
		val query_ : String = this.query // query_ 后有空格
		rdd.filter(x => x.contains(query_))
	}
}
