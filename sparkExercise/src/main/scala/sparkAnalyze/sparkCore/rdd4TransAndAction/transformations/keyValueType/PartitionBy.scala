package sparkAnalyze.sparkCore.rdd4TransAndAction.transformations.keyValueType

import org.apache.spark.{SparkConf, SparkContext}

object PartitionBy {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("Chapter5_1_2_1")
    val sc = new SparkContext(conf)

    val rddData1 = sc.parallelize(Array(("Alice", 18), ("Bob", 3), ("Thomas", 20)), 2)
    import org.apache.spark.HashPartitioner
    val rddData2 = rddData1.partitionBy(new HashPartitioner(5))

    println(rddData2.partitions.length)

    sc.stop()
  }
}
