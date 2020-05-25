package sparkAnalyze.sparkSQL

import java.util

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types.{DataType, DoubleType, IntegerType, LongType, StringType, StructField, StructType}

/*
UDAF(User Defined Aggregation Function):用户自定义聚合函数，
可接收并才处理多个参数（如一列中的多个行中的值），之后返回一个值；如自带的sum函数
 */
object UDAF {
	def main(args: Array[String]): Unit = {
		val spark: SparkSession = SparkSession.builder().config(new SparkConf().setMaster("local[*]").setAppName("UDAF")).getOrCreate()
		import spark.implicits._

		// 自定义schema
		val schema = StructType(List(
			StructField("name", StringType, nullable = false),
			StructField("age", IntegerType, nullable = false),
			StructField("gender", StringType, nullable = false)
		))
		val javaList = new util.ArrayList[Row]()
		javaList.add(Row("Tony", 10, "Male"))
		javaList.add(Row("Alice", 20, "Female"))
		javaList.add(Row("Tom", 18, "Male"))
		javaList.add(Row("Boris", 30, "Male"))
		val dataF: DataFrame = spark.createDataFrame(javaList, schema)

		dataF.show()
		dataF.createTempView("user")
		spark.sql("SELECT sum(age) age, gender FROM user GROUP BY gender").show()

		// 注册并调用UDAF
		spark.udf.register("toDouble", (column: Any) => column.toString.toDouble)
		spark.udf.register("avgAge", AverageUDAF)

		spark.sql("SELECT gender, cast(avgAge(toDouble(age)) as decimal(10, 2)) as avgAge FROM user GROUP BY gender")
		  .show()

		spark.stop()

	}

}

/**
 * 求用户的平均年龄
 * SELECT gender, avgUDAF(age) FROM people GROUP BY age
 */
object AverageUDAF extends UserDefinedAggregateFunction {
	// UDAF输入的数据类型
	override def inputSchema: StructType = {
		StructType(
			// UDAF如果需要接收多个参数，直接在StructType中添加多个StructField
			// StructField（类型名称，数据类型，是否可为空
			StructField("numInput", DoubleType, nullable = true)
			  :: Nil
		)
	}
	// 设置UDAF在聚合过程中的缓冲区保存的数据的类型
	override def bufferSchema: StructType = {
		// 求平均数过程中会累加：年龄总和，参与累加的人数
		StructType(
			StructField("buffer1", DoubleType, nullable = true) // 年龄总和
			  :: StructField("buffer2", LongType, nullable = true) // 参与累加人数
			  :: Nil
		)
	}
	// UDAF运行结束返回的数据的类型
	override def dataType: DataType = DoubleType
	// 判断UDAF可接收的参数类型和返回结果的类型是否一直
	// 此处接收的年龄为Double，返回的平均年龄为Double
	override def deterministic: Boolean = true
	// 初始化在聚合过程中所使用的聚合缓冲区
	override def initialize(buffer: MutableAggregationBuffer): Unit = {
		buffer(0) = 0.0 // 年龄总和
		buffer(1) = 0L // 参与累加的人数
	}
	// 控制具体的聚合逻辑
	/*
	在同一个分区中，每次只取出一行数据。该行数据的内容不一定包含所有的列，只包含SELECT查询所涉及的列。
	第一列的值存放在ROW中小标为0的位置上，以此类推
	将原表中的每一行参与运算的列累加到聚合缓冲区的Row实列中
	 */
	override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
		buffer(0) = buffer.getDouble(0) + input.getDouble(0) // 年龄
		buffer(1) = buffer.getLong(1) + 1 // 参与累加人数
	}
	// 将每个分区的聚合缓冲区的数据合并到一个聚合缓冲区中
	override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
		buffer1(0) = buffer1.getDouble(0) + buffer2.getDouble(0)
		buffer1(1) = buffer1.getLong(1) + buffer2.getLong(1)
	}
	// 对最终聚合缓冲区的数据进行最后一次运算，得到UDAF的最终结果
	override def evaluate(buffer: Row): Any = {
		buffer.getDouble(0) / buffer.getLong(1)
	}
}
