package sparkAnalyze.sparkCore.practice.sparkMall.application

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import sparkAnalyze.sparkCore.practice.sparkMall.common.model.UserVisitAction

/*
需求：指定页面的单跳转率
假设一个用户在一个Session中访问的页面路劲为2-4-5-6-8-10
那么，从2-4为一次单跳，6-8为一次单跳；单页转换率就是统计页面点击的概率，
2-4的单跳转化率：页面2的PV为A，访问完页面2后跳转到页面4的PV为B，B/A则为2-4的单页转换率
 */
object PageFlow {
	def main(args: Array[String]): Unit = {
		val sc = new SparkContext(new SparkConf().setMaster("local[*]").setAppName("PageFlowRate"))

		val userActiveData: RDD[String] = sc.textFile("data/project/user_visit_action.csv")

		val visitData: RDD[UserVisitAction] = userActiveData.map {
			line => {
				val datas: Array[String] = line.split(",")
				UserVisitAction(
					datas(0),
					datas(1),
					datas(2),
					datas(3).toLong,
					datas(4),
					datas(5),
					datas(6).toLong,
					datas(7).toLong,
					datas(8),
					datas(9),
					datas(10),
					datas(11),
					datas(12)
				)
			}
		}

		// 反复使用的数据可以缓存起来
		visitData.cache()

		// 过滤原始数据，保留需要统计的页面数据
		val pageIds = List(1, 2, 3, 4, 5, 6, 7)
		// 指定需要统计的跳转页面
		val staticPageZip: List[String] = pageIds.zip(pageIds.tail).map { case (pageIds1, pageIds2) => pageIds1 + "-" + pageIds2 }
		val filterRDD: RDD[UserVisitAction] = visitData.filter(data => pageIds.contains(data.page_id.toInt))

		// 转换数据结构：(pageId, 1)
		val mapRDD: RDD[(Long, Long)] = filterRDD.map(data => (data.page_id, 1L))

		// 聚合并转换格式为Map：(pageId, 1) ---> (pageId, sum) ---> Map[Long, Long]
		// 指定跳转页面的单页PV（分母）
		val pageId2Sum: Map[Long, Long] = mapRDD.reduceByKey(_ + _).collect().toMap

		//------------------------------------------------------------------------------------------------------

		// 对Session进行分组：(session, Iterator[UserVisitAction])
		val sessionGroup: RDD[(String, Iterable[UserVisitAction])] = visitData.groupBy(data => data.session_id)

		// 对分组后的数据按访问时间进行升序排列,并将页面格式化为1-2，2-3的形式:(session, List[pageIdZip])
		val session2Zip: RDD[(String, List[(String, Long)])] = sessionGroup.mapValues(datas => {
			val actions: List[UserVisitAction] = datas.toList.sortWith((left, right) => left.action_time < right.action_time)
			val pageIds: List[Long] = actions.map(_.page_id)
			val zipList: List[(Long, Long)] = pageIds.zip(pageIds.tail) // ((1,2),(2,3))

			zipList.map {
				case (pageId1, pageId2) => (pageId1 + "-" + pageId2, 1L)
			}
		})

		// 将List[pageId]拆散:List((2-7,1), (7-12,1), (12-18,1)) ---> ((2-7,1), (7-12,1), (12-18,1))
		val zipPageIds: RDD[(String, Long)] = session2Zip.map(_._2).flatMap(list => list)

		// 过滤非指定页面数据
		val zipFilterRDD: RDD[(String, Long)] = zipPageIds.filter { case (pageFlow, one) => staticPageZip.contains(pageFlow) }

		// 过滤完的数据聚合：(pageId1-pageId2, sum) 得访问完页面1后接着访问页面2的PV（分子）
		val pageFlowReduceRDD: RDD[(String, Long)] = zipFilterRDD.reduceByKey(_ + _)

		// 计算转换率
		pageFlowReduceRDD.foreach {
			case (pageFlow, sum) => {
				val pageId1: String = pageFlow.split("-")(0)
				val result: Double = sum.toDouble / pageId2Sum.getOrElse(pageId1.toLong, 1L)
				println(f"$pageFlow = $result%.4f")
			}
		}

		sc.stop()
	}
}
