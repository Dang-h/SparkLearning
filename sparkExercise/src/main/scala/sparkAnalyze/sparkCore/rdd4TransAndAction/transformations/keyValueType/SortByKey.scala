package sparkAnalyze.sparkCore.rdd4TransAndAction.transformations.keyValueType

import org.apache.spark.{SparkConf, SparkContext}

object SortByKey {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("Chapter5_1_2_7")
    val sc = new SparkContext(conf)


    val rddData1 = sc.parallelize(Array(("dog", 3), ("cat", 1), ("hadoop", 2), ("spark", 3), ("apple", 2)))
    val rddData2 = rddData1.sortByKey()
    println(rddData2.collect.mkString(","))

    sc.stop()
  }
}
