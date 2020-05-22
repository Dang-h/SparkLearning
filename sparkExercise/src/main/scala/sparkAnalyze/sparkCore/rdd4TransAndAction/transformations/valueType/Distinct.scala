package sparkAnalyze.sparkCore.rdd4TransAndAction.transformations.valueType

import org.apache.spark.{SparkConf, SparkContext}


object Distinct {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("Chapter5_1_1_4")
    val sc = new SparkContext(conf)

    val rddData = sc.parallelize(Array("Alice", "Nick", "Alice", "Kotlin", "Catalina", "Catalina"), 3)
    val rddData2 = rddData.distinct
    println(rddData2.collect.mkString(","))


    sc.stop()
  }
}
