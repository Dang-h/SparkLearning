package sparkAnalyze.sparkSQL

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession, UDFRegistration}

/*
UDF(User Defined Function)：接收一个参数（比如视图中某一列值），返回一个结果（返回一个处理后的值）；
 */
object UDF {
	def main(args: Array[String]): Unit = {
		// 将表中”name”列用户名全部转为大写
		val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("UDF")
		val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()

		import spark.implicits._
		val inputData: RDD[User] = spark.sparkContext.textFile("data/example/people.txt")
		  .map(l => {
			  val data: Array[String] = l.split(",")
			  User(data(0).toInt, data(1), data(2).trim.toInt)
		  })
		val data: Dataset[User] = inputData.toDS()

		data.show()
		data.createTempView("people")

		// 创建UDF
		val udf: UDFRegistration = spark.udf
		udf.register("toUpperCase", (column: String) => column.toUpperCase)
		// 使用
		spark.sql("select id, toUpperCase(name) name, age from people").show()

		spark.stop()
	}
}
