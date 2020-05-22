package sparkAnalyze.sparkCore.rdd4TransAndAction.transformations.keyValueType

import org.apache.spark.{SparkConf, SparkContext}

object MapValues {
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
