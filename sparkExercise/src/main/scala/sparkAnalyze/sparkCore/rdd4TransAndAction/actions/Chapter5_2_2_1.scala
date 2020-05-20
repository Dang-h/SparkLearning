package sparkAnalyze.sparkCore.rdd4TransAndAction.actions

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
object Chapter5_2_2_1 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("Chapter5_2_2_1")
    val sc = new SparkContext(conf)

    val rddData1 = sc.parallelize(
      Array(
        ("用户1", "接口1"),
        ("用户2","接口1"),
        ("用户1", "接口1"),
        ("用户1", "接口2"),
        ("用户2", "接口3")),
      2)

    println(rddData1.lookup("用户1").mkString(","))

    sc.stop()
  }
}
