package sparkAnalyze.sparkCore.rdd4Advance

import org.apache.spark.{SparkConf, SparkContext}

/**
  *
  * FUNCTIONAL_DESCRIPTION: 
  * CREATE_BY: 尽际
  * CREATE_TIME: 2019/2/27 18:32
  * MODIFICATORY_DESCRIPTION: 
  * MODIFY_BY:
  * MODIFICATORY_TIME:
  * VERSION：V1.0
  */
object Chapter6_3_2_2_2 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("Chapter6_3_2_2_2")
    val sc = new SparkContext(conf)

    import org.apache.spark.HashPartitioner

    val rddData1 = sc.parallelize(Array(("Alice", 15), ("Bob", 18), ("Thomas", 20), ("Catalina", 25)))
    val rddData2 = sc.parallelize(Array(("Alice", "Female"), ("Thomas", "Male"), ("Tom", "Male")))

    val rddData3 = rddData1.partitionBy(new HashPartitioner(3))
    val rddData4 = rddData2.partitionBy(new HashPartitioner(3))

    val rddData5 = rddData3.join(rddData4, 2)
    println(rddData5.collect.mkString(","))

  }
}
