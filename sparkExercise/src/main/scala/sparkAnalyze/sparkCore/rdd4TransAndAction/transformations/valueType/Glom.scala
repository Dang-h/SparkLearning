package sparkAnalyze.sparkCore.rdd4TransAndAction.transformations.valueType

import org.apache.spark.{SparkConf, SparkContext}

object Glom {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("Chapter5_1_1_13")
    val sc = new SparkContext(conf)

    val rddData1 = sc.parallelize(1 to 10, 5)
    val rddData2 = rddData1.glom

    rddData2.collect().foreach(arr => println(arr.mkString(",")))
    sc.stop()
  }
}
