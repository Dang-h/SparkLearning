package sparkAnalyze.sparkCore.rdd4TransAndAction.actions

import org.apache.spark.{SparkConf, SparkContext}

object Top {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("Chapter5_2_1_4")
    val sc = new SparkContext(conf)

    val rddData1 = sc.parallelize(Array(("Alice", 95), ("Tom", 75), ("Thomas", 88)), 2)
    println(rddData1.top(2)(Ordering.by(t => t._2)))

    sc.stop()
  }
}
