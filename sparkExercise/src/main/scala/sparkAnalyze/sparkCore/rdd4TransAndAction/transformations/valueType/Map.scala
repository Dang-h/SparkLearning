package sparkAnalyze.sparkCore.rdd4TransAndAction.transformations.valueType

import org.apache.spark.{SparkConf, SparkContext}

object Map {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("Chapter5_1_1_1")
    val sc = new SparkContext(conf)

    val rddData = sc.parallelize(1 to 10)
    val rddData2 = rddData.map(_ * 10)
    println(rddData2.collect.mkString(","))

    sc.stop()
  }
}
