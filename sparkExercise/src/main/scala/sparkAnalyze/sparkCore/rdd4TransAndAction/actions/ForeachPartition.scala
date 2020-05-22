package sparkAnalyze.sparkCore.rdd4TransAndAction.actions

import org.apache.spark.{SparkConf, SparkContext}

object ForeachPartition {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("Chapter5_2_1_10")
    val sc = new SparkContext(conf)

    val rddData1 = sc.parallelize(Array(5, 5, 15, 15), 2)
    rddData1.foreachPartition(iter => {
      while (iter.hasNext) {
        val element = iter.next()
        println(element)
      }
    })

    sc.stop()
  }
}
