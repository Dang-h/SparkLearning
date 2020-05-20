package sparkAnalyze.sparkCore.rdd4TransAndAction.transformations

import org.apache.spark.{SparkConf, SparkContext}

/**
  * FUNCTIONAL_DESCRIPTION:
  * CREATE_BY: 尽际
  * CREATE_TIME: 2019/2/27 10:00
  * MODIFICATORY_DESCRIPTION:
  * MODIFY_BY:
  * MODIFICATORY_TIME:
  * VERSION：V1.0
  */
object Chapter5_1_2_10 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("Chapter5_1_2_10")
    val sc = new SparkContext(conf)

    val rddData1 = sc.parallelize(
      Array(
        ("Alice", 3500),
        ("Bob", 2000),
        ("Thomas", 10000),
        ("Catalina", 2000),
        ("Kotlin", 4000),
        ("Karen", 9000)), 2)
    val rddData2 = rddData1.mapValues(_ + "元")

    println(rddData2.collect.mkString(","))

    sc.stop()
  }
}
