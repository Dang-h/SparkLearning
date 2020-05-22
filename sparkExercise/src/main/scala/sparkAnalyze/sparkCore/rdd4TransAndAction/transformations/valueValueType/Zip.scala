package sparkAnalyze.sparkCore.rdd4TransAndAction.transformations.valueValueType

import org.apache.spark.{SparkConf, SparkContext}

object Zip {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("Chapter5_1_1_14")
    val sc = new SparkContext(conf)

    val rddData1 = sc.parallelize(1 to 3, 2)
    val rddData2 = sc.parallelize(Array("A", "B", "C"), 2)
    val rddData3 = rddData1.zip(rddData2)

    println(rddData3.collect.mkString(","))

    sc.stop()
  }
}
