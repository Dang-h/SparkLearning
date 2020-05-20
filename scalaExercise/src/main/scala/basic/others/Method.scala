package basic.others

import java.io.File

import scala.io.{BufferedSource, Source}

object Method {
	def main(args: Array[String]): Unit = {
		val file = new File("D:\\Develop\\Coding\\sparkExercise\\data\\words.txt")
		val source: BufferedSource = Source.fromFile(file)
		val lines: Iterator[String] = source.getLines()
		val lineList: List[String] = lines.toList
		val lineSplit: List[String] = lineList.flatMap(line => line.trim.split("\\s+"))
		val wordGroup: Map[String, List[String]] = lineSplit.groupBy(word => word)
		val word2Count: Map[String, Int] = wordGroup.map((word: (String, List[String])) => {
			(word._1, word._2.size)
		})


		println(lineList)
		println(lineSplit)
		println(wordGroup)
		println(word2Count)
	}
}
