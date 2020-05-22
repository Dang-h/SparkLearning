package sparkAnalyze.sparkCore.rdd4TransAndAction.transformations.keyValueType

import org.apache.spark.{SparkConf, SparkContext}

object FlatMapValues {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("Chapter5_1_2_11")
    val sc = new SparkContext(conf)

    val rddData1 = sc.parallelize(
      Array(
        ("文件A", "cat\tdog\thadoop\tcat"),
        ("文件A", "cat\tdog\thadoop\tspark"),
        ("文件B", "cat\tspark\thadoop\tcat"),
        ("文件B", "spark\tdog\tspark\tc)t")),
      2)

    val rddData2 = rddData1.flatMapValues(_.split("\t"))

    val rddData3 = rddData2.map((_, 1))

    val rddData4 = rddData3.reduceByKey(_ + _).sortBy(_._1._1)

    println(rddData4.collect.mkString(","))

      sc.stop()
  }
}
