import java.io.File

import scala.io.Source

object WordCount {
	def main(args: Array[String]): Unit = {
		val file = new File("D:\\Develop\\Coding\\sparkExercise\\data\\words.txt")
		val data: List[String] = Source.fromFile(file).getLines().toList

		val listLine: List[(String, Int)] = List(("Hello Scala", 4), ("Hello Spark", 3), ("Spark Scala", 2))

		wordCount1(data)
		wordCount2(data)
		wordCount3(listLine)
	}

	def wordCount1(data: List[String]) = {
		val wordMap = scala.collection.mutable.Map[String, Int]()

		data.foreach(line => line.split("\\s+").foreach(word => {
			if (wordMap.contains(word)) {
				wordMap(word) += 1
			} else {
				wordMap += (word -> 1)
			}
		}))

		wordMap
	}

	def wordCount2(data: List[String]) = {
		val wordList: List[String] = data.flatMap(line => line.split("\\s+"))
		// 相同单词分在同一组
		val wordGroupMap: Map[String, List[String]] = wordList.groupBy(word => word)
		val wordCountMap: Map[String, Int] = wordGroupMap.map(word => (word._1, word._2.size))

		wordCountMap
	}

	def wordCount3(data: List[(String, Int)]) = {
		// 将("Spark Scala", 2) --> ("Spark Scala Spark Scala")
		val lineList: List[String] = data.map(word => {
			(word._1 + " ") * word._2
		})
		// 将("Spark Scala Spark Scala") --> ("Spark","Scala","Spark","Scala)
		val wordList: List[String] = lineList.flatMap(line => line.split(" "))
		// 将("Spark","Scala","Spark","Scala) --> Map(Spark -> List(Spark,Spark), Scala -> (Scala,Scala))
		val wordGroupMap: Map[String, List[String]] = wordList.groupBy(word => word)
		// 将Map(Spark -> List(Spark,Spark), Scala -> (Scala,Scala)) --> Map(Spark -> 2, Scala -> 2)
		val word2Count: Map[String, Int] = wordGroupMap.map(word => (word._1, word._2.size))
		word2Count
	}

	def wordCount4(data: List[(String, Int)]) = {
		// 将("Spark Scala", 2) --> List((Spark,2), (Scala,2))
		val word2CountList: List[(String, Int)] = data.flatMap {
			case (line, count) => {
				val words: Array[String] = line.split(" ") // words为局部变量
				words.map(word => (word, count)) // word为匿名参数,count为模式匹配值
			}
		}
		// 将相同单词的tuple合并 List((Spark,2),(Spark,2)) --> Map(spark -> List((Spark,2),(Spark,2)))
		val word2CountMap: Map[String, List[(String, Int)]] = word2CountList.groupBy {
			case (word, wordList) => word
		}
		// 将Map(spark -> List((Spark,2),(Spark,2))) --> Map(Spark -> List(2,2))
		val mergeCountMap: Map[String, List[Int]] = word2CountMap.mapValues(wordList => {
			// 青色的Map为类型参数
			wordList.map { case (word, countList) => countList }
		})
		// 将Map(Spark -> List(2,2)) --> Map(Spark -> 4)
		val word2Count: Map[String, Int] = mergeCountMap.map { case (word, count) => (word, count.sum) }
		word2Count
	}
}
