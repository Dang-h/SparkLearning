package sparkAnalyze.sparkSQL.orderAnalyze

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Dataset, SparkSession}

object sql {
	def main(args: Array[String]): Unit = {
		val spark = SparkSession.builder().config(new SparkConf().setMaster("local[*]").setAppName("orderAnalyze")).getOrCreate()

		val tbStockRDD: RDD[String] = spark.sparkContext.textFile("data/practice/tbStock.txt")
		val tbDateRDD: RDD[String] = spark.sparkContext.textFile("data/practice/tbDate.txt")
		val tbStockDetailRDD: RDD[String] = spark.sparkContext.textFile("data/practice/tbStockDetail.txt")

		import spark.implicits._

		// 处理数据，转换结果
		val tbStockDS: Dataset[tbStock] = tbStockRDD.map(_.split(",")).map(arr => tbStock(arr(0), arr(1), arr(2))).toDS()
		val tbDateDS: Dataset[tbDate] = tbDateRDD.map(_.split(",")).map(arr =>
			tbDate(
				arr(0),
				arr(1).toInt,
				arr(2).toInt,
				arr(3).toInt,
				arr(4).toInt,
				arr(5).toInt,
				arr(6).toInt,
				arr(7).toInt,
				arr(8).toInt,
				arr(9).toInt)).toDS()
		val tbStockDetailDS: Dataset[tbStockDetail] = tbStockDetailRDD.map(_.split(",")).map(arr => tbStockDetail(
			arr(0),
			arr(1).toInt,
			arr(2),
			arr(3).toInt,
			arr(4).toDouble,
			arr(5).toDouble
		)).toDS()

		// 添加临时视图
		tbStockDS.createOrReplaceTempView("tbStock")
		tbDateDS.createOrReplaceTempView("tbDate")
		tbStockDetailDS.createOrReplaceTempView("tbStockDetail")

		//分析
		println("-------------------------计算所有订单中每年的销售单数、销售总额-------------------------")
		/*
		SELECT t2.theyear, count(DISTINCT t.ordernumber) AS cnt_order, sum(t.amount) AS sum_amount
		FROM tbStock
         JOIN tbstockdetail t
              ON tbStock.ordernumber = t.ordernumber
         JOIN tbdate t2
              ON tbStock.dateid = t2.dateid
		GROUP BY t2.theyear
		ORDER BY t2.theyear;
		 */
		spark.sql("SELECT t2.theyear, count(DISTINCT t.ordernumber) AS cnt_order, sum(t.amount) AS sum_amount FROM " +
		  "tbStock         JOIN tbstockdetail t               ON tbStock.ordernumber = t.ordernumber         JOIN " +
		  "tbdate t2              ON tbStock.dateid = t2.dateid GROUP BY t2.theyear ORDER BY t2.theyear").show()

		println("-------------------------所有订单每年最大金额订单的销售额-------------------------")
		spark.sql("SELECT theyear, max(t1.total_amount) AS total_amount FROM (SELECT ts.dateid, tSD.ordernumber, sum(tSD.amount) AS total_amount       FROM tbStock ts                JOIN tbStockDetail tSD ON ts.ordernumber = tSD.ordernumber       GROUP BY ts.dateid, tSD.ordernumber) t1          JOIN tbDate td ON t1.dateid = td.dateid GROUP BY theyear ORDER BY theyear DESC")
  		  .show()

		println("-------------------------所有订单中每年最畅销货品-------------------------")
		spark.sql("WITH t1 AS (SELECT theyear, itemid, sum(tSD.amount) AS total_amount             FROM tbStock tS                      JOIN tbStockDetail tSD ON tS.ordernumber = tSD.ordernumber                      JOIN tbDate tD ON tS.dateid = tD.dateid             GROUP BY tD.theyear, tSD.itemid) SELECT DISTINCT t1.theyear, itemid, max_amount FROM t1          JOIN (SELECT theyear, max(total_amount) AS max_amount                FROM t1                GROUP BY theyear) t2               ON t1.theyear = t2.theyear AND t1.total_amount = t2.max_amount")
  		  .show()

		spark.stop()
	}
}
