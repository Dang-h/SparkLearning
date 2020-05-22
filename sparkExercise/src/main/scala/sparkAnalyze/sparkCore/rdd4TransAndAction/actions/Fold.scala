package sparkAnalyze.sparkCore.rdd4TransAndAction.actions

import org.apache.spark.{SparkConf, SparkContext}

object Fold {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("Chapter5_2_1_8")
    val sc = new SparkContext(conf)

    val rddData1 = sc.parallelize(Array(5, 5, 15, 15), 2)
    val result = rddData1.fold(1)((x, y) => x + y)
    println(result)

    sc.stop()
  }
}
