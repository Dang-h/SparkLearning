package sparkAnalyze.sparkCore.rdd4TransAndAction.actions

import org.apache.spark.{SparkConf, SparkContext}

object Collect {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("Chapter5_2_1_1")
    val sc = new SparkContext(conf)

    val rddData1 = sc.parallelize(1 to 5)
    println(rddData1.collect.mkString(","))

    sc.stop()
  }
}
