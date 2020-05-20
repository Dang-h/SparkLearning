package basic

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

object Test {
	def main(args: Array[String]): Unit = {
		val sentence = "AAAAAAAAAABBBBBBBBCCCCCDDDDDDD"
		val arr = ArrayBuffer[Char]()

		def putArr(arr: ArrayBuffer[Char], c: Char) = {
			arr.append(c)
			arr
		}

		sentence.foldLeft(arr)(putArr)
		println("arr=" + arr)
		println("==========================")

		val arr1 = ArrayBuffer[Char]()
		for (elem <- sentence.toArray) {
			arr1.append(elem)
		}
		println("arr1 = " + arr1)
		println("==========================")

		// 统计相同字母个数
		val charToChars: Map[Char, ArrayBuffer[Char]] = arr.groupBy(char => char)
		val charToCount: Map[Char, Int] = charToChars.map(char => (char._1, char._2.size))
		println(charToCount)
		println("-------------------------")

		def charCount(map: Map[Char, Int], c: Char) = {
			map + (c -> (map.getOrElse(c, 0) + 1))
		}
		println(sentence.foldLeft(Map[Char,Int]())(charCount))

	}

}
