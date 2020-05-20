package sparkAnalyze.sparkCore.rdd4Advance

import org.apache.spark.{SparkConf, SparkContext}

// 定义User样例类
// 样例类会自动生成set/get/toString方法
case class User1(name: String, telephone: String)

/*
  集合累加器
  将手机尾号为“3个重复数字”的用户添加到积极和累加器中
  */
object AccumulatorTest2 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("Accumulator2")
    val sc = new SparkContext(conf)

    val userArray = Array(User1("Alice", "15837312345"),
      User1("Bob", "13937312666"),
      User1("Thomas", "13637312345"),
      User1("Tom", "18537312777"),
      User1("Boris", "13837312998"))

    val userRDD = sc.parallelize(userArray, 2)
    val userAccumulator = sc.collectionAccumulator[User1]("用户累加器")

    userRDD.foreach(user => {
      val telephone = user.telephone.reverse
      if (telephone(0) == telephone(1) && telephone(0) == telephone(2)) {
        userAccumulator.add(user)
      }
    })

    println(userAccumulator)

  }
}
