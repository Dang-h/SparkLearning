package sparkAnalyze.sparkCore.rdd4TransAndAction.transformations.valueValueType

import org.apache.spark.{SparkConf, SparkContext}

object ZipWithUniqueId {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("Chapter5_1_1_17")
    val sc = new SparkContext(conf)

    val rddData1 = sc.parallelize(Array("A", "B", "C", "D", "E"), 2)
    val rddData2 = rddData1.zipWithUniqueId()
    println(rddData2.collect.mkString(","))
    

    sc.stop()
  }
}
