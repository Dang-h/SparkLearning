package sparkAnalyze.sparkCore.rdd4TransAndAction.actions

import org.apache.spark.{SparkConf, SparkContext}

object Count {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("Chapter5_2_1_11")
    val sc = new SparkContext(conf)

    val rddData1 = sc.parallelize(Array(("语文", 95), ("数学", 75), ("英语", 88)), 2)
    println(rddData1.count)

    sc.stop()
  }
}
