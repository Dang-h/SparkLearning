package basic.others

import scala.collection.mutable.ArrayBuffer

object CollectionDemo {
	def main(args: Array[String]): Unit = {
		// 变长数组
		val arrBuffer = ArrayBuffer[Int]()
		// 追加元素
		arrBuffer.append(7)
		arrBuffer.append(1, 2, 3)
		// 重新赋值
		arrBuffer(0) = 2
		for (elem <- arrBuffer) {
			println(elem)
		}

		println("==========================")
		// 多维数组（3行4列的数组）
		val arr2Dim = Array.ofDim[Int](3, 4)
		arr2Dim(0)(0) = 1
		arr2Dim(0)(1) = 2
		arr2Dim(0)(2) = 3
		arr2Dim(0)(3) = 4
		for (elem1 <- arr2Dim) {
			for (elem <- elem1) {
				print(elem + " ")
			}
			println()
		}
		println("==========================")

		// Scala集合和Java集合互转
		val arrScala: ArrayBuffer[String] = ArrayBuffer("1", "2", "3", "4")
		import scala.collection.JavaConversions.bufferAsJavaList // 隐式转换，将Buffer =》 java.util.List
		val arrJava = new ProcessBuilder(arrScala) // ProcessBuilder是Java的一个构造器
		val arrList = arrJava.command()
		println(arrList)
		println("==========================")

		// Java的List转Scala的Array
		import scala.collection.JavaConversions.asScalaBuffer
		import scala.collection.mutable
		// java.util.Lost => Buffer
		val scalaArr = arrList
		scalaArr.append("Tom")
		println(scalaArr)
		println("==========================")

		// 列表List
		val list = List(1, 2, 3, 4)
		val list1: List[Int] = list :+ 5 // :位于集合的一侧
		val list2: List[Int] = 6 +: list
		val list3: List[Int] = 7 :: 8 :: list // ::在列表的开头添加元素
		for (elem <- list3) {
			print(elem + " ")
		}
		println()
		val list4 = list3 :: list // ::在列表的开头添加元素,List(7, 8, 1, 2, 3, 4) 1 2 3 4
		val list5 = list3 ::: list // :::将元素添加到给定List前,7 8 1 2 3 4 1 2 3 4
		val list6 = list3 ::: list ::: Nil // :::将元素添加到给定List前,7 8 1 2 3 4 1 2 3 4
		for (elem <- list4) {
			print(elem + " ")
		}
		println("\n" + list4)
		for (elem <- list5) {
			print(elem + " ")
		}
		println("\n" + list5)
		for (elem <- list6) {
			print(elem + " ")
		}
		println("\n" + list6)
		println("==========================")

		// Queue
		val que1 = new mutable.Queue[Int]()
		// 追加元素
		que1 += 1
		que1 += 2
		que1 += 3
		que1 ++ list // 类似于单个+，将两个集合合并起来
		que1 ++= List(4, 5, 6) // 类似于+=
		println(que1)
		println("==========================")

		val map = Map("one" -> 1, "two" -> 2, "three" -> 3)
		val map1 = Map(("a", 1), ("b", 2), ("c", 3))
		val mapMut = scala.collection.mutable.Map(1 -> "one", 2 -> "two", 3 -> "three")
		val mapMut1 = mutable.Map((1, "a"), (2, "b"), (3, "c"))
		println(map)
		println(map1)
		println(mapMut)
		println(mapMut1.get(1))
		println(mapMut1.get(4))
		println(mapMut1(1))
		println(mapMut1.get(1).get)
		println(map.getOrElse("d", "Unknown"))
		for (elem <- mapMut.keys) {
			print(elem + " ")
		}
		println()
		for (elem <- mapMut.values) {
			print(elem + " ")
		}
		println()
		for (elem <- mapMut) {
			print(elem + " ")
		}
		println("==========================")

		println(list.map(_ * 2)) // list中的每个元素*2
		println(list.map((n: Int) => n * 2))
		println(list.reduce(_ + _))
		println(list.fold(0)(_ + _))
		println("==========================")

		// stream
		// Stream集合中存放BigInt的值，numFrom为函数名，
		// 集合创建的第一个元素是n，后续元素生成规则为n+1
		// #:: 表示构造一个由给定的第一个元素和来自延迟计算流的元素组成的流。
		def numFrom(n: BigInt): Stream[BigInt] = n #:: numFrom(n + 1)

		def multi(x: BigInt): BigInt = x * x
	}
}
