package sparkAnalyze.sparkSQL

import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, Dataset, Encoder, Row, SparkSession}
import org.apache.spark.sql.types.{StringType, StructField, StructType}

import scala.collection.mutable

/*
UDTF(User Defined Table generating Functions)：用户自定义表生成函数
可以将一行中的某一列数据展开，实现一进多出
 */
object UDTF {
	def main(args: Array[String]): Unit = {
		val spark: SparkSession = SparkSession.builder().config(new SparkConf().setMaster("local[*]").setAppName("UDTF")).getOrCreate()

		val schema: StructType = StructType(List(
			StructField("movie", StringType, nullable = false),
			StructField("category", StringType, nullable = false)
		))

		val javaList = new java.util.ArrayList[Row]()
		javaList.add(Row("《疑犯追踪》", "战争,动作,科幻"))
		javaList.add(Row("《叶问》", "动作,战争"))
		val df1 = spark.createDataFrame(javaList, schema)

		df1.show

		// flatMap返回结果是Dataset[(String, String)]的数据集，需要通过编码器Encoder编码
		implicit val flatMapEncoder: Encoder[(String, String)] = org.apache.spark.sql.Encoders.kryo[(String, String)]
		val tableArr: Array[(String, String)] = df1.flatMap(row => {
			val listTuple = new mutable.ListBuffer[(String, String)]()
			val category: Array[String] = row.getString(1).split(",")
			for (elem <- category) {
				listTuple.append((row.getString(0), elem))
			}
			listTuple
		}).collect()

		val result: DataFrame = spark.createDataFrame(tableArr).toDF("movie", "category")
		result.show()

		spark.stop()
	}
}
