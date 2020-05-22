package sparkAnalyze.sparkCore.rdd4TransAndAction.transformations.valueValueType

import org.apache.spark.{SparkConf, SparkContext}

object Union {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("Chapter5_1_1_7")
    val sc = new SparkContext(conf)

    val rddData1 = sc.parallelize(1 to 10)
    val rddData2 = sc.parallelize(1 to 20)
    val rddData3 = rddData1.union(rddData2)

    println(rddData3.collect.mkString(","))
    sc.stop()
  }
}
