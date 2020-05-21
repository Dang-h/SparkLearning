package sparkAnalyze.sparkCore.practice.sparkMall.application

import java.util.UUID

import org.apache.spark.rdd.RDD
import org.apache.spark.util.AccumulatorV2
import org.apache.spark.{SparkConf, SparkContext}
import sparkAnalyze.sparkCore.practice.sparkMall.common.model.CategoryTop10
import sparkAnalyze.sparkCore.practice.sparkMall.common.utils.StrUtil

import scala.collection.{immutable, mutable}

object CategoryTop10App4Accumulator {
	def main(args: Array[String]): Unit = {
		val sc = new SparkContext(new SparkConf().setMaster("local[*]").setAppName("categoryTop104Accumulator"))

		val inputData: RDD[String] = sc.textFile("data/project/user_visit_action.csv")

		val acc = new CategoryAccumulator1
		// 注册累加器
		sc.register(acc, "accumulator")

		// 使用累加器
		// 返回的结果样式：(category_click, sum)
		inputData.foreach {
			line => {
				val datas: Array[String] = line.split(",")
				if (datas(6) != "-1") {
					acc.add(datas(6) + "_click")
				} else if (StrUtil.isNotEmpty(datas(8))) {
					val categoryIds: Array[String] = datas(8).split("-")
					categoryIds.map(id => acc.add(id + "_order"))
				} else if (StrUtil.isNotEmpty(datas(10))) {
					val categoryIds: Array[String] = datas(10).split("-")
					categoryIds.map(id => (acc.add(id + "_pay")))
				}
			}
		}

		// 获取累加器的值
		val accValue: mutable.HashMap[String, Long] = acc.value

		// 对(category_click, sum) 进行分组 --> ((category,(click, sum)),(category,(order, sum)))
		val category2Map: Map[String, mutable.HashMap[String, Long]] = accValue.groupBy {
			case (k, sum) => k.split("_")(0)
		}


		// 转换为样例类
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

		// 排序，取前10
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

		result.foreach(println)

		sc.stop()
	}
}

/* 品类数据统计局累加器
	输入的数据类型：String(用户点击行为的日期,用户的 ID,Session ID,某个页面的 ID,点击行为的时间点,用户搜索的关键词,点击的商品品类的 ID,点击的商品的 ID,一次订单中所有品类的 ID 集合,一次订单中所有商品的 ID 集合,一次支付中所有品类的 ID 集合,一次支付中所有商品的 ID 集合)
	输出的数据形式：((category_click, 100), (category_pay, 100),...)
	输出数据类型为mutable.HashMap[String, Long]以便于后续处理
 */
class CategoryAccumulator1 extends AccumulatorV2[String, mutable.HashMap[String, Long]] {

	// 创建一个空Map装数据
	private var map = new mutable.HashMap[String, Long]()

	// 判断累加器是否处于初始状态
	override def isZero: Boolean = {
		map.isEmpty
	}
	// 累加器复制
	override def copy(): AccumulatorV2[String, mutable.HashMap[String, Long]] = {
		new CategoryAccumulator1
	}
	// 重置累加器
	override def reset(): Unit = {
		map.clear()
	}
	// 累加数据到累加器
	override def add(v: String): Unit = {
		// k存在返回key对应的值，不存在则返回默认值（0）
		map(v) = map.getOrElse(v, 0L) + 1
	}
	// 对不同分区累加器整合
	override def merge(other: AccumulatorV2[String, mutable.HashMap[String, Long]]): Unit = {
		var map1 = map
		var map2 = other.value

		map = map1.foldLeft(map2) {
			(tempMap, kv) => {
				val k: String = kv._1
				val v: Long = kv._2
				tempMap(k) = tempMap.getOrElse(k, 0L) + v

				tempMap
			}
		}
	}
	// 返回累加器的结果
	override def value: mutable.HashMap[String, Long] = {
		map
	}
}
