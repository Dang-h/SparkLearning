package sparkAnalyze.sparkStreaming.timeDuration

import com.alibaba.fastjson.{JSON, TypeReference}
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable

/*
时间概念理解：批处理时间间隔；窗口时间宽度；滑动时间宽度
窗口时间宽度与滑动时间宽度不可分割，需要一同使用；并且它们的大小必须是批处理的间隔的整数倍
 */
object TimeDuration {
	/*
	从端口中持续读取JSON格式的城市温度数据，并根据温度变化持续统计10s内每个城市的平均温度
	 */
	def main(args: Array[String]): Unit = {
		val sc = new SparkContext(new SparkConf().setMaster("local[*]").setAppName("Time Duration"))
		// Seconds(2)就是批处理时间间隔，每间隔2秒产生一个RDD
		val ssc = new StreamingContext(sc, Seconds(2))

//		ssc.checkpoint("./spark_Streaming")

		// 数据格式：{"city":"北京","temperature":"12.5"}
		val jsonDStream: ReceiverInputDStream[String] = ssc.socketTextStream("hadoop100", 9999)

		val cityAndTemperatureDStream = jsonDStream.map(json => {
			// 解析JSON，并将其转换为java.Map[String， String]
			val json2Map = JSON.parseObject(json, new TypeReference[java.util.Map[String, String]](){})

			import scala.collection.JavaConverters._

			// 将java.Map转换为Scala.Map
			val json2ScalaMap: mutable.Map[String, String] = json2Map.asScala
			json2ScalaMap
		})

		// (city, (sumTemperature, count))
		val sumOfTemperatureAndCountDS: DStream[(String, (Float, Int))] = cityAndTemperatureDStream
		  // 提取city和temperature --> (city, temperature)
		  .map(scalaMap => (scalaMap("city"), scalaMap("temperature")))
		  // (city,(temperature, 1))
		  .mapValues(temperature => (temperature.toFloat, 1))

		  // 利用窗口操作，将数据按照城市聚合，每个城市的温度数据与条目分别累加
//		  /*
		  // 第一次聚合=T1+T2+T3+T4+T5
		  // 第二次聚合=T2+T3+T4+T5+T6
		  // T2~T5参与了两次聚合
		  .reduceByKeyAndWindow(
			  // （温度+温度，1+1）
			  (t1: (Float, Int), t2: (Float, Int)) => (t1._1 + t2._1, t1._2 + t2._2),
			  Seconds(10), // 指定窗口宽度为10（批处理间隔的5倍）
			  Seconds(2) // 指定滑动窗口为2（批处理间隔的1倍）==>每2s统计10s内的平均温度
		  )
//		   */

		// 优化：第二次聚合=第一次聚合 + T6 - T1
//  		.reduceByKeyAndWindow(
//			(t1: (Float, Int), t2: (Float, Int)) => (t1._1 + t2._1, t1._2 + t2._2), //reduceFuc
//			(t1: (Float, Int), t2: (Float, Int)) => (t1._1 + t2._1, t1._2 + t2._2), // invReduceFuc
//			Seconds(10),
//			Seconds(2)
//		)

		// 求窗口内的平均温度(city, avgTemperature)
		val resultDS: DStream[(String, Float)] = sumOfTemperatureAndCountDS.mapValues(x => x._1 / x._2)
		resultDS.print()

		ssc.start()
		ssc.awaitTermination()
	}
}
