package sparkAnalyze.sparkCore.rdd4TransAndAction.transformations.valueType

import org.apache.spark.{SparkConf, SparkContext}

object RandomSplit {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("Chapter5_1_1_12")
    val sc = new SparkContext(conf)

    val rddData1 = sc.parallelize(1 to 10, 3)
    val splitRDD = rddData1.randomSplit(Array(1, 4, 5))

    println(splitRDD(0).collect.mkString(","))
    println(splitRDD(1).collect.mkString(","))
    println(splitRDD(2).collect.mkString(","))

    sc.stop()
  }
}
