package sparkAnalyze.sparkCore.noteThat

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object PartitionerTest {
	def main(args: Array[String]): Unit = {
		val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("PartitionerTest")
		val sc = new SparkContext(conf)

		val nopar: RDD[(Int, Int)] = sc.parallelize(List((1, 3), (1, 2), (2, 4), (2, 3), (3, 6), (3, 8)), 8)
		val collect: Array[String] = nopar.mapPartitionsWithIndex((index, iter) => Iterator(index.toString + ":" + iter.mkString("|")))
		  .collect
		for (elem <- collect) {
			println(elem)
		}

		val customerPars: RDD[(Int, Int)] = nopar.partitionBy(new CustomerPartitioner(2))

		val customerParsArr: Array[(Int, (Int, Int))] = customerPars.mapPartitionsWithIndex((index, iter) => iter.map((index, _))).collect
		for (elem <- customerParsArr) {
			println(elem)
		}

		sc.stop()
	}
}

class CustomerPartitioner(numPars:Int) extends org.apache.spark.Partitioner {

	// 覆盖分区数
	override def numPartitions: Int = numPars

	// 覆盖分区号获取函数
	override def getPartition(key: Any): Int = {
		val ckey = key.toString
		ckey.substring(ckey.length - 1).toInt % numPars
	}
}
