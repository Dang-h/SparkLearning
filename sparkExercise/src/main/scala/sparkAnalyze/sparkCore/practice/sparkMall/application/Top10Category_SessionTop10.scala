package sparkAnalyze.sparkCore.practice.sparkMall.application

import java.sql.{Connection, DriverManager, PreparedStatement}
import java.util.UUID

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import sparkAnalyze.sparkCore.practice.sparkMall.common.model.{CategoryTop10, CategoryTop10SessionTop10, UserVisitAction}
import sparkAnalyze.sparkCore.practice.sparkMall.common.utils.StrUtil

import scala.collection.{immutable, mutable}

/*
需求：Top10 品类中Top10 活跃的Session
CREATE TABLE `category_top10_session_count`
(
    `taskId`     Text,
    `categoryId` Text,
    `sessionId`  Text,
    `clickCount` Bigint(20) DEFAULT NULL
) ENGINE = INNODB
  DEFAULT CHARSET = utf8
COMMENT 'Top10品类中Top10活跃Session';
 */
object Top10Category_SessionTop10 {
	def main(args: Array[String]): Unit = {
		val sc = new SparkContext(new SparkConf().setMaster("local[*]").setAppName("CategoryTop10Session"))
		val data: RDD[String] = sc.textFile("data/project/user_visit_action.csv")

		// 创建累加器
		val acc = new CategoryAccumulator1
		sc.register(acc, "accumulator")

		data.foreach {
			line: String => {
				val datas: Array[String] = line.split(",")
				if (datas(6) != "-1") {
					acc.add(datas(6) + "_click")
				} else if (StrUtil.isNotEmpty(datas(8))) {
					val categoryId: Array[String] = datas(8).split("-")
					categoryId.map(id => acc.add(id + "_order"))
				} else if (StrUtil.isNotEmpty(datas(10))) {
					val categoryId: Array[String] = datas(10).split("-")
					categoryId.map(id => acc.add(id + "_pay"))
				}
			}
		}

		val accValue: mutable.HashMap[String, Long] = acc.value

		val category2Map: Map[String, mutable.HashMap[String, Long]] = accValue.groupBy {
			case (k, sum) => k.split("_")(0) // 取得categoryId
		}
		val taskId: String = UUID.randomUUID().toString
		val categoryTop10: immutable.Iterable[CategoryTop10] = category2Map.map {
			case (category, map) => {
				CategoryTop10(
					taskId,
					category,
					map.getOrElse(category + "_click", 0L),
					map.getOrElse(category + "_order", 0L),
					map.getOrElse(category + "_pay", 0L)
				)
			}
		}

		val result: List[CategoryTop10] = categoryTop10.toList.sortWith {
			(left, right) => {
				if (left.clickCount > right.clickCount) {
					true
				} else if (left.clickCount == right.clickCount) {
					if (left.orderCount > right.orderCount) {
						true
					} else if (left.orderCount == right.orderCount) {
						if (left.payCount > right.payCount) {
							true
						} else {
							false
						}
					} else {
						false
					}
				} else {
					false
				}
			}
		}.take(10)

		//===================== 需求二 ==============================

		// 原始数据转化为样例类
		val dataRdd: RDD[UserVisitAction] = data.map {
			line => {
				val datas: Array[String] = line.split(",")
				UserVisitAction(
					datas(0),			// 用户点击行为的日期
					datas(1),			// 用户的 ID
					datas(2),			// Session ID
					datas(3).toLong,	// 某个页面的 ID
					datas(4),			// 点击行为的时间点
					datas(5),			// 用户搜索的关键词
					datas(6).toLong,	// 点击的商品品类的 ID
					datas(7).toLong,	// 点击的商品的 ID
					datas(8),			// 一次订单中所有品类的 ID 集合
					datas(9),			// 一次订单中所有商品的 ID 集合
					datas(10),			// 一次支付中所有品类的 ID 集合	,
					datas(11),			// 一次支付中所有商品的 ID 集合	,
					datas(12)			// 城市ID
				)
			}
		}

		// top10(任务id,品类ID,点击次数,下单次数,支付次数)
		val categoryIds: List[String] = result.map(top10 => top10.categoryId)

		// 广播变量
		val broadcastIds: Broadcast[List[String]] = sc.broadcast(categoryIds)

		// 返回Top10的相关数据
		val filterRDD: RDD[UserVisitAction] = dataRdd.filter {
			data => {
				if (data.click_category_id == -1) {
					false
				} else {
					broadcastIds.value.contains(data.click_category_id.toString)
				}
			}
		}

		// 过滤后的数据转换结构：(category_session, 1)
		val mapRDD: RDD[(String, Long)] = filterRDD.map {
			data => {
				(data.click_category_id + "_" + data.session_id,1L)
			}
		}

		// 聚合：(category_session, 1) --> (category_session, sum)
		val reduceRDD: RDD[(String, Long)] = mapRDD.reduceByKey(_ + _)

		// 转换结构以便统计：(category_session, sum) --> (category, (session, sum))
		val mapRDD1: RDD[(String, (String, Long))] = reduceRDD.map {
			case (k, sum) => {
				val categorySession: Array[String] = k.split("_")
				(categorySession(0), (categorySession(1), sum))
			}
		}

		// 分组统计:(category, (session, sum)) --> (category, Iterator[(session, sum)])
		val groupRDD: RDD[(String, Iterable[(String, Long)])] = mapRDD1.groupByKey()

		// 取出分组后数据的value：Iterator[(session, sum)]，降序排列取前10
		val resultRDD: RDD[(String, List[(String, Long)])] = groupRDD.mapValues(datas => {
			datas.toList.sortWith {
				(left, right) => (left._2 > right._2)
			}.take(10)
		})

		// 将数据转换为样例类
		val top10SessionTop10: RDD[List[CategoryTop10SessionTop10]] = resultRDD.map {
			case (categoryId, list) => {
				list.map {
					case (sessionId, sum) => CategoryTop10SessionTop10(taskId, categoryId, sessionId, sum)
				}
			}
		}

		val sessionTop10: RDD[CategoryTop10SessionTop10] = top10SessionTop10.flatMap(list => list)
		// flatten：将二维数组的所有行连接到一个数组中:Array(List((1,2,3),(4,5,6))) ---> Array((1,2,3), (4,5,6))
//		val sessionTop10: Array[CategoryTop10SessionTop10] = top10SessionTop10.flatten

		// 结果保存到MySQL
		sessionTop10.foreachPartition(datas => {
			val driver = "com.mysql.jdbc.Driver"
			val url = "jdbc:mysql://hadoop100:3306/sparkExercise"
			val userName = "root"
			val passWd = "mysql"

			Class.forName(driver)
			val connection: Connection = DriverManager.getConnection(url, userName, passWd)
			val sql = "insert into category_top10_session_count ( taskId, categoryId, sessionId, clickCount) values (?, ?, ?, ?)"
			val statement: PreparedStatement = connection.prepareStatement(sql)

			datas.foreach{
				obj =>{
					statement.setObject(1, obj.taskId)
					statement.setObject(2, obj.categoryId)
					statement.setObject(3, obj.sessionId)
					statement.setObject(4, obj.clickCount)
					statement.executeUpdate()
				}
			}

			statement.close()
			connection.close()
		})


		sc.stop()
	}
}
