package sparkAnalyze.sparkCore.practice.sparkMall.application

import java.sql.{Connection, DriverManager, PreparedStatement}
import java.util.UUID

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import sparkAnalyze.sparkCore.practice.sparkMall.common.model.CategoryTop10
import sparkAnalyze.sparkCore.practice.sparkMall.common.utils.StrUtil

/*
需求：取点击、下单和支付数量排名前 10 的品类并将计算结果存入MySQL

CREATE TABLE `category_top10` (
`taskId` text,
`category_id` text,
`click_count` bigint(20) DEFAULT NULL,
`order_count` bigint(20) DEFAULT NULL,
`pay_count` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8
 */
object CategoryTop10App {
	def main(args: Array[String]): Unit = {
		val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("CategoryTop10")
		val sc = new SparkContext(conf)

		// 获取数据
		/*
		日志格式：
		2019-05-05,85,e2eef06e-beaa-4b49-acaf-38e057e1cd6e,8,2019-05-05 02:02:38,,-1,-1,,,1-2-3,1-2-3,20
			0  date              	用户点击行为的日期				2019-05-05
			1  user_id           	用户的 ID					85
			2  session_id        	Session ID					e2eef06e-beaa-4b49-acaf-38e057e1cd6e
			3  page_id           	某个页面的 ID					8
			4  action_time       	点击行为的时间点				2019-05-05 02:02:38
			5  search_keyword    	用户搜索的关键词				null
			6  click_category_id 	点击的商品品类的 ID			-1
			7  click_product_id  	点击的商品的 ID				-1
			8  order_category_ids	一次订单中所有品类的 ID 集合	null
			9  order_product_ids 	一次订单中所有商品的 ID 集合	null
			10 pay_category_ids  	一次支付中所有品类的 ID 集合	1-2-3
			11 pay_product_ids   	一次支付中所有商品的 ID 集合	1-2-3
			12 city_id				城市ID						20
		 */
		val dataRDD: RDD[String] = sc.textFile("data/project/user_visit_action.csv")

		// 转化数据结构：(category_click, 1)
		val mapRDD: RDD[Array[(String, Long)]] = dataRDD.map(line => {
			val datas: Array[String] = line.split(",")
			if (datas(6) != "-1") { // 点击
				Array((datas(6) + "_click", 1L))
			} else if (StrUtil.isNotEmpty(datas(8))) { // 下单
				val categoryIds: Array[String] = datas(8).split("-")
				categoryIds.map(id => (id + "_order", 1L))
			} else if (StrUtil.isNotEmpty(datas(10))) { // 支付
				val categoryIds: Array[String] = datas(10).split("-")
				categoryIds.map(id => (id + "_pay", 1L))
			} else {
				Array(("", 0L))
			}
		})

		//				mapRDD.collect.take(3).foreach(_.foreach(println))

		// 将Array((category_id_click,1))拆散 --> (category_id_click, 1)
		val flatMapRDD: RDD[(String, Long)] = mapRDD.flatMap(data => data)

		// 过滤掉key为空的数据据
		val filterRDD: RDD[(String, Long)] = flatMapRDD.filter {
			case (key, v) => StrUtil.isNotEmpty(key)
		}

		//  数据分组聚合(category_id_click, 1) --> (category_id_click, sum) 得到同一品类点击的总次数
		val reduceRDD: RDD[(String, Long)] = filterRDD.reduceByKey(_ + _)

		// 转换聚合后数据结构： (category_id_click, sum) -->((category_id),(click, sum)) 便于将相同的品类聚合
		val mapRDD1: RDD[(String, (String, Long))] = reduceRDD.map {
			case (key, sum) => {
				val keys: Array[String] = key.split("_")
				(keys(0), (keys(1), sum))
			}
		}

		// 对数据进行分组：((category_id),(click, sum))  --> ((category_id),Iterator[click, sum])
		val groupRDD: RDD[(String, Iterable[(String, Long)])] = mapRDD1.groupByKey()

		// 将分组后数据转换成样例类((category_id),Iterator[click, sum]) --> UserActiveAction
		val taskId: String = UUID.randomUUID().toString
		val classRDD: RDD[CategoryTop10] = groupRDD.map {
			case (categoryId, iter) => {
				val map: Map[String, Long] = iter.toMap
				CategoryTop10(taskId, categoryId, map.getOrElse("click", 0L), map.getOrElse("order", 0L), map
				  .getOrElse("pay", 0L))
			}
		}

		// 将转换后的数据进行降序排列，取前10
		//				val categoryTop10Arr: Array[CategoryTop10] = classRDD.sortBy(obj => obj.orderCount, false).take(10)
		// 不使用sortBy是因为它不能处理数据相等的情况
		val categoryTop10Arr: Array[CategoryTop10] = classRDD.collect.sortWith {
			(left, right) => {
				if (left.clickCount > right.clickCount) {
					true
				} else if (left.clickCount == right.clickCount) {
					if (left.orderCount > right.orderCount) {
						true
					} else if (left.orderCount == right.orderCount) {
						left.payCount > right.payCount
					} else {
						false
					}
				} else {
					false
				}
			}
		}.take(10)

		//		categoryTop10Arr.foreach(println)

		// 将结果保存到MySQL
		val driver = "com.mysql.jdbc.Driver"
		val url = "jdbc:mysql://hadoop100:3306/sparkExercise"
		val userName = "root"
		val passWd = "mysql"
		Class.forName(driver)
		val connection: Connection = DriverManager.getConnection(url, userName, passWd)
		val sql = "insert into category_top10 (taskId, category_id, click_count, order_count, pay_count) values (?,?, ?, ?, ?)"
		val statement: PreparedStatement = connection.prepareStatement(sql)

		categoryTop10Arr.foreach{
			obj => {
				statement.setObject(1, obj.taskId)
				statement.setObject(2, obj.categoryId)
				statement.setObject(3, obj.clickCount)
				statement.setObject(4, obj.orderCount)
				statement.setObject(5, obj.payCount)
				statement.executeUpdate()
			}
		}
		statement.close()
		connection.close()

		sc.stop()
		println("Success!")
	}
}
