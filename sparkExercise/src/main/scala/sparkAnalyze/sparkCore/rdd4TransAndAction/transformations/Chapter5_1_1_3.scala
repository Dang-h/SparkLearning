package sparkAnalyze.sparkCore.rdd4TransAndAction.transformations

import org.apache.spark.{SparkConf, SparkContext}

/**
  * FUNCTIONAL_DESCRIPTION:
  * CREATE_BY: 尽际
  * CREATE_TIME: 2019/2/27 10:00
  * MODIFICATORY_DESCRIPTION:
  * MODIFY_BY:
  * MODIFICATORY_TIME:
  * VERSION：V1.0
  */
object Chapter5_1_1_3 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("Chapter5_1_1_3")
    val sc = new SparkContext(conf)

    val rddData = sc.parallelize(1 to 100)
    import scala.util.control.Breaks._
    val rddData2 = rddData.filter(n => {
      var flag = if (n < 2) false else true
      breakable {
        for (x <- 2 until n) {
          if (n % x == 0) {
            flag = false
            break
          }
        }
      }
      flag
    })

    println(rddData2.collect.mkString(","))

    sc.stop()
  }
}
