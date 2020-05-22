package sparkAnalyze.sparkCore.rdd4TransAndAction.transformations.valueType

import org.apache.spark.{SparkConf, SparkContext}

object MapPartitions {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("Chapter5_1_1_5")
    val sc = new SparkContext(conf)

    val rddData = sc.parallelize(
      Array(
        ("201800001", 83),
        ("201800002", 97),
        ("201800003", 100),
        ("201800004", 95),
        ("201800005", 87)),
      2)

    val rddData2 = rddData.mapPartitions(iter => {
      var result = List[String]()
      while (iter.hasNext) {
        result = iter.next() match {
          case (id, grade) if grade >= 95 => id + "_" + grade :: result
          case _ => result
        }
      }
      result.iterator
    })

    println(rddData2.collect.mkString(","))

    sc.stop()
  }
}
