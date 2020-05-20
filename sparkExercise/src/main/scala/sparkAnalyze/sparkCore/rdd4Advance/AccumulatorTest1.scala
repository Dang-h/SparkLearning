package sparkAnalyze.sparkCore.rdd4Advance

import org.apache.spark.util.LongAccumulator
import org.apache.spark.{SparkConf, SparkContext}

/**
 * 长整、双精度浮点累加器——longAccumulator
 */
object AccumulatorTest1 {
  def main(args: Array[String]): Unit = {
    val sc = new SparkContext(new SparkConf().setMaster("local[*]").setAppName("Accumulator1"))

    val visitorRDD = sc.parallelize(
      Array(
        ("Bob", 15),
        ("Thomas", 28),
        ("Tom", 18),
        ("Galen", 35),
        ("Catalina", 12),
        ("Karen", 9),
        ("Boris", 20)),
      3)

    // 给累加器取名
    val visitAccumulator: LongAccumulator = sc.longAccumulator("统计成年游客人数")

    // 执行Action算子，如果满足18岁及以上，累加器+1
    // foreach是在Worker端分配的Executor进程中执行，
    /*
     var count = 0L
     visitorRDD.foreach(visitor => {
      if(visitor._2 >= 18) {
        count += 1
      }
    })
     println(count)
     在Driver进程中定义的count变量不会被累加器累加
     在Executor进程中定义的count变量才会被累加
     */
    visitorRDD.foreach(visitor => {
      if(visitor._2 >= 18) {
        visitAccumulator.add(1)
      }
    })

    // 打印累加器中内容
    println(visitAccumulator)
    // 打印累加器中的值
    println(visitAccumulator.value)
  }
}
