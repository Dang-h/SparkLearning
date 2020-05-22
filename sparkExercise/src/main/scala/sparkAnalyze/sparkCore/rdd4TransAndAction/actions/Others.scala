package sparkAnalyze.sparkCore.rdd4TransAndAction.actions

import org.apache.spark.{SparkConf, SparkContext}

object Others {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("Chapter5_2_3")
    val sc = new SparkContext(conf)

    val numRDD = sc.parallelize(1 to 10)
    println(numRDD.sum)
    println(numRDD.max)
    println(numRDD.min)
    println(numRDD.mean)
    println(numRDD.variance)
    println(numRDD.sampleVariance)
    println(numRDD.stdev)
    println(numRDD.sampleStdev)

    sc.stop()
  }
}
