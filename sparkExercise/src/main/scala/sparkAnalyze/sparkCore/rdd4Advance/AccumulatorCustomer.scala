package sparkAnalyze.sparkCore.rdd4Advance

import org.apache.spark.rdd.RDD
import org.apache.spark.util.AccumulatorV2
import org.apache.spark.{SparkConf, SparkContext}

/*
数据格式：姓名、性别、年龄
使用累加器分别统计男性用户数、女性用户数、11岁及以下年纪用户数、12~17岁用户数
 */
object AccumulatorCustomer {
	def main(args: Array[String]): Unit = {
		val sc = new SparkContext(new SparkConf().setMaster("local[*]").setAppName("AccumulatorCustomer"))

		val userArray = Array(User2("Alice", "Female", 11),
			User2("Bob", "Male", 16),
			User2("Thomas", "Male", 14),
			User2("Catalina", "Female", 20),
			User2("Boris", "Third", 12),
			User2("Karen", "Female", 9),
			User2("Tom", "Male", 7)
		)

		val userRDD: RDD[User2] = sc.parallelize(userArray, 2)
		val userAccumulator = new UserAccumulator[User2] // 如果者只为lazy；懒执行模式（在REPL中不会立即执行，）
		// 注册累加器
		sc.register(userAccumulator, "CustomerAccumulator")

		userRDD.foreach(userAccumulator.add)
		println(userAccumulator)

		sc.stop()
	}
}

case class User2(name: String, sex: String, age: Int)

// [T, Array[Int]] 指定两种泛型：待累加数据类型T（这里是User2），累加后返回的数据类型Array[Int](此处存放的都是统计的数据)
class UserAccumulator[T] extends AccumulatorV2[T, Array[Int]] {
	// Array[Int](男性用户数、女性用户数、其他性别、12岁以下年纪用户数、12~17岁用户数)
	private val resArr: Array[Int] = Array(0, 0, 0, 0, 0)

	// 判断累加器是否处于初始状态
	override def isZero: Boolean = resArr.mkString("").toLong == 0L

	// 实现累加器的复制，通过该方法可以复制一个相同状态的累加器
	override def copy(): AccumulatorV2[T, Array[Int]] = {
		val newAcc = new UserAccumulator[T]
		newAcc
	}

	// 将累加器中的全部结果归为初始状态
	override def reset(): Unit = {
		for (i <- resArr.indices) {
			resArr(i) = 0
		}
	}

	// 将数据累加到累加器中
	override def add(v: T): Unit = {
		val user: User2 = v.asInstanceOf[User2] // 将类型V转换成类型User2
		// 按性别累加
		if (user.sex == "Female") {
			resArr(0) += 1
		} else if (user.sex == "Male") {
			resArr(1) += 1
		} else {
			resArr(2) += 1
		}
		// 按年龄累加
		if (user.age < 12) {
			resArr(3) += 1
		} else if (user.age < 18) {
			resArr(4) += 1
		}
	}

	// 将不同分区中的累加器合并
	override def merge(other: AccumulatorV2[T, Array[Int]]): Unit = {
		val otherAcc: UserAccumulator[T] = other.asInstanceOf[UserAccumulator[T]]
		resArr(0) += otherAcc.resArr(0)
		resArr(1) += otherAcc.resArr(1)
		resArr(2) += otherAcc.resArr(2)
		resArr(3) += otherAcc.resArr(3)
		resArr(4) += otherAcc.resArr(4)
	}

	// 返回当前累加器结果
	override def value: Array[Int] = {
		resArr
	}
	override def toString(): String = {
		getClass.getSimpleName + s"(id: $id, name: $name, value: ${value.mkString(",")})"
	}
}
