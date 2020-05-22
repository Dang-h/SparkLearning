package sparkAnalyze.sparkCore.rdd4TransAndAction.transformations.keyValueType

import org.apache.spark.{SparkConf, SparkContext}

object AggregateByKey {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("Chapter5_1_2_5")
    val sc = new SparkContext(conf)

    val rddData1 = sc.parallelize(
      Array(
        ("用户1", "接口1"),
        ("用户2", "接口1"),
        ("用户1", "接口1"),
        ("用户1", "接口2"),
        ("用户2", "接口3")),
      2)

    val rddData2 = rddData1.aggregateByKey(collection.mutable.Set[String]())(
      (urlSet, url) => urlSet += url,
      (urlSet1, urlSet2) => urlSet1 ++= urlSet2)

    println(rddData2.collect.mkString(","))

    sc.stop()
  }
}
