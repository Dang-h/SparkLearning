package sparkAnalyze.sparkSQL

import java.sql.Date
import java.util
import java.util.Properties

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Dataset, Row, SaveMode, SparkSession}

object CreateDF_DS {
	def main(args: Array[String]): Unit = {

		val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("createDF&DS")
		// 创建SparkSQL环境对象
		val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()

		import spark.implicits._
		// 创建RDD
		val rdd: RDD[(Int, String, Int)] = spark.sparkContext.parallelize(List((1, "zhangsan", 30), (2, "lisi", 40)))

		println("-------------------------通过序列集合创建DataFrame-------------------------------------")
		// 通过序列集合创建DataFrame
		val dfFromList: DataFrame = spark.createDataFrame(List(
			("Alice", "Female", "20"),
			("Tom", "Male", "25"),
			("Boris", "Male", "18"))
		).toDF("name", "gender", "age")
		dfFromList.show()

		println("-------------------------自定义Schema------------------------------------------------")
		// 自定义Schema约束每列的数据类型及列名，并可指定列支是否为空
		val schema: StructType = StructType(List(
			// 列名，数据类型，是否允许空置
			StructField("name", StringType, true),
			StructField("age", IntegerType, true),
			StructField("sex", StringType, true)
		))
		// 实例化一个Java的List集合，泛型为Row
		val javaList = new util.ArrayList[Row]()
		javaList.add(Row("Alice", 20, "Female"))
		javaList.add(Row("Tom", 18, "Male"))
		javaList.add(Row("Boris", 30, "Male"))
		val dfFromCustomSchema: DataFrame = spark.createDataFrame(javaList, schema)
		dfFromCustomSchema.show()

		println("-------------------------相互转换------------------------------------------------")
		val dfFromRDD: DataFrame = rdd.toDF("id", "name", "age")
		val dsFromDF: Dataset[User] = dfFromRDD.as[User]
		dfFromRDD.show()
		dsFromDF.show()

		dfFromRDD.rdd.foreach(println)
		println("---------------------")
		dsFromDF.rdd.foreach(println)

		println("------------------------------读取MySQL中数据---------------------------------------")
		val options = Map(
			"url" -> "jdbc:mysql://hadoop100:3306/sparkExercise",
			"driver" -> "com.mysql.jdbc.Driver",
			"user" -> "root",
			"password" -> "mysql",
			"dbtable" -> "category_top10"
		)
		val dfFromMysql: DataFrame = spark.read.format("jdbc").options(options).load()
		dfFromMysql.show()

		println("------------------------------输出数据到MySQL---------------------------------------")
		val properties = new java.util.Properties()
		properties.setProperty("user", "root")
		properties.setProperty("password", "mysql")
		// 写入模式有4种：Overwrite:覆写；Append：追加；Ignore：不存在则创建，存在则忽略；ErrorIfExists：存在则抛异常
		dfFromList.write.mode(SaveMode.Overwrite).jdbc("jdbc:mysql://hadoop100:3306/sparkExercise", "sparkSQLTest", properties)

		println("------------------------------输出数据为Parquet文件---------------------------------------")
		// repartition重分区目的是将输出文件保存为一个文件
		// spark加载数据和输出默认为parquet
		dfFromList.repartition(1).write.format("parquet")
//		  .save("hdfs://hadoop100:9000/user/test/parquet")
		  .save("file:///Develop/Coding/offerGoExercise/data/output/parquet")
		println("Done")

		spark.stop()
	}
}

case class User(id: Int, name: String, age: Int)
