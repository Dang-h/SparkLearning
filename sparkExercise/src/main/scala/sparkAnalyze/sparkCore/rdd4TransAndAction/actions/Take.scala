package sparkAnalyze.sparkCore.rdd4TransAndAction.actions

import org.apache.spark.{SparkConf, SparkContext}

object Take {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("Chapter5_2_1_3")
    val sc = new SparkContext(conf)

    val rddData1 = sc.parallelize(Array("Thomas", "Alice", "Kotlin"))
    println(rddData1.take(2))

    sc.stop()
  }
}
