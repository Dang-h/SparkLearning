package sparkAnalyze.sparkStreaming.dsTreamOpration

import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

import scala.util.{Success, Try}

/*
将当前DStream中某种类型的RDD通过f转换成另一种类型，并封装到新的RDD中；一般用于对RDD直接进行操作
 */
object Transform {
	def main(args: Array[String]): Unit = {
		val sc = new SparkContext(new SparkConf().setMaster("local[*]").setAppName("transform"))
		val ssc = new StreamingContext(sc, Seconds(5))

		val lineDStream: ReceiverInputDStream[String] = ssc.socketTextStream("hadoop100", 9999)

		// 筛选出消费金额大于3的完整乘车数据（StationA，StationB, 4）
		// transform操作在Driver中执行
		val resultDStream: DStream[String] = lineDStream.transform(rdd => {
			val arrRDD: RDD[Array[String]] = rdd.map(_.split(","))
			val filterRDD: RDD[Array[String]] = arrRDD.filter(arr => {
				if (arr.length == 3) {
					Try(arr(2).toInt) match {
						case Success(_) if arr(2).toInt > 3 => true
						case _ => false
					}
				} else {
					false
				}
			})

			val resultRDD: RDD[String] = filterRDD.map((_: Array[String]).mkString(","))
			resultRDD
		})

		// foreachRDD(f)的f在Driver中被调用
		resultDStream.foreachRDD(rdd =>
			rdd.foreachPartition((p: Iterator[String]) =>
				p.foreach(result =>
					println(result)
				)
			)
		)

		ssc.start()
		ssc.awaitTermination()
	}
}
