package sparkAnalyze.sparkCore.practice.sparkMall.application

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import sparkAnalyze.sparkCore.practice.sparkMall.common.model.UserVisitAction
import sparkAnalyze.sparkCore.practice.sparkMall.common.utils.DateParse

/*
需求：
页面平均停留时间TOP10统计
 */
object PageAvgAccessTime {
	def main(args: Array[String]): Unit = {
		val sc = new SparkContext(new SparkConf().setMaster("local[*]").setAppName("PageAvgAccessTime"))

		val inputData: RDD[String] = sc.textFile("data/project/user_visit_action.csv")

		val lineDataRDD: RDD[UserVisitAction] = inputData.map(line => {
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
		})

		// 转换数据结构：(session, (pageId, actionTime))
		val rdd: RDD[(String, (Long, String))] = lineDataRDD.map(action => (action.session_id, (action.page_id, action.action_time)))

		// 分组：(session, Iter[(pageId, actionTime)])
		val groupRDD: RDD[(String, Iterable[(Long, String)])] = rdd.groupByKey()

		// 分组后数据转换格式得到每个页面访问的时长：(session, List(pageId, time))
		val subTimeRDD: RDD[(String, List[(Long, Long)])] = groupRDD.mapValues(data => {
			// 分组后数据按照action_time升序排列：(session, List[(pageId, actionTime)])
			val sortList: List[(Long, String)] = data.toList.sortWith((left, right) => left._2 < right._2)

			//排序后数据对pageId和actionTime做拉链处理：((pageId1, actionTime1),(pageId1, actionTime1)..)
			val zipList: List[((Long, String), (Long, String))] = sortList.zip(sortList.tail)

			zipList.map {
				case (action1, action2) => {
					val time1: Long = DateParse.parseString2Timestamp(action1._2)
					val time2: Long = DateParse.parseString2Timestamp(action2._2)

					(action1._1, time2 - time1)
				}
			}
		})

		// 将List(pageId, time)取出拆散成（pageId, time）
		val flatMapRDD: RDD[(Long, Long)] = subTimeRDD.map(_._2).flatMap(list => list)

		// 根据pageId进行分组(pageId, CompactBuffer(time))
		val pageIdGroupRDD: RDD[(Long, Iterable[Long])] = flatMapRDD.groupByKey()

		// 计算结果
		val pageIdTimeRDD: RDD[(Long, Long)] = pageIdGroupRDD.map {
			case (pageId, timeList) => {
				(pageId, timeList.sum / timeList.size)
			}
		}
		pageIdTimeRDD.sortBy(_._2,false).take(10).foreach{
			case(pageId, time) => println(f"页面$pageId 平均停留时间为: $time")
		}

	}
}
