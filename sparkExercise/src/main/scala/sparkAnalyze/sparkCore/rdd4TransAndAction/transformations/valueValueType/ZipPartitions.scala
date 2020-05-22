package sparkAnalyze.sparkCore.rdd4TransAndAction.transformations.valueValueType

import org.apache.spark.{SparkConf, SparkContext}

object ZipPartitions {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("Chapter5_1_1_15")
    val sc = new SparkContext(conf)

    val rddData1 = sc.parallelize(1 to 10, 2)
    val rddData2 = sc.parallelize(20 to 25, 2)
    val rddData3 = rddData1.zipPartitions(rddData2)((rddIter1, rddIter2) => {
      var result = List[(Int, Int)]()
      while (rddIter1.hasNext && rddIter2.hasNext) {
        result ::= (rddIter1.next(), rddIter2.next())
      }
      result.iterator
    })

    println(rddData3.collect.mkString(","))

    sc.stop()
  }
}
