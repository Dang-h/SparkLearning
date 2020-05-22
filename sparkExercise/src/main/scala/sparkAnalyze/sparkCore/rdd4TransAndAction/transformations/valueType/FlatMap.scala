package sparkAnalyze.sparkCore.rdd4TransAndAction.transformations.valueType

import org.apache.spark.{SparkConf, SparkContext}

object FlatMap {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("Chapter5_1_1_2")
    val sc = new SparkContext(conf)

    val rddData = sc.parallelize(Array("one,two,three", "four,five,six", "seven,eight,nine,ten"))
    val rddData2 = rddData.flatMap(_.split(","))
    println(rddData2.collect.mkString(","))

    sc.stop()
  }
}
