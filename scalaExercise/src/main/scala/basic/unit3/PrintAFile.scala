package basic.unit3

import scala.io.Source

object PrintAFile {
	def main(args: Array[String]): Unit = {
		val lines = Source.fromFile("D:\\Develop\\Coding\\sparkExercise\\scalaExercise\\src\\main\\scala\\basic" +
		  "\\Frock.scala").getLines().toList
		val longestLine = lines.reduceLeft(
			(a, b) => if (a.length > b.length) a else b
		)
		val maxLength = widthOgLength(longestLine)
		for (elem <- lines) {
			val numSpaces = maxLength - widthOgLength(elem)
			val padding = " " * numSpaces
			println(padding + elem.length + " | " + elem)
		}

	}

	def widthOgLength(s: String) = s.length.toString.length

}
