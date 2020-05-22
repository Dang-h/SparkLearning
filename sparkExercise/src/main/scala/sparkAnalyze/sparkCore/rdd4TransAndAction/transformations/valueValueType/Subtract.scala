package sparkAnalyze.sparkCore.rdd4TransAndAction.transformations.valueValueType

import org.apache.spark.{SparkConf, SparkContext}

object Subtract {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("Chapter5_1_1_9")
    val sc = new SparkContext(conf)

    val rddData1 = sc.parallelize(Array(1, 1, 2))
    val rddData2 = sc.parallelize(Array(2, 2, 3))
    val rddData3 = rddData1.subtract(rddData2)

    println(rddData3.collect.mkString(","))

    sc.stop()
  }
}
