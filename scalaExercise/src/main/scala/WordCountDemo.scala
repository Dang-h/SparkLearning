import java.io.File

import scala.io.Source

object WordCountDemo {
	def main(args: Array[String]): Unit = {
		val dirFile = new File("D:\\Develop\\Coding\\sparkExercise\\data")
		val files: Array[File] = dirFile.listFiles
		for (file <- files) {
			println(file)
		}
		val listFiles = files.toList
		val wordsMap = scala.collection.mutable.Map[String, Int]()
		// file =>Source.fromFile(file).getLines().foreach(...)是一个匿名函数
		// 功能是把file变量和列表listFiles的元素进行绑定，遍历listFiles中的元素，把获取的值赋值给file，
		// 然后把file作为入参传递给Source.fromFile(file).getLines().foreach(...)处理
		// Source.fromFile(file).getLines()会以行的形式读取file内容，然后通过.foreach(...)对每一行内容进行处理
		// line=>line.split(" ").foreach(...)
		// 功能是把file变量和Source.fromFile(file).getLines()
		// 得到的所有行的集合进行绑定，每次遍历获取集合中的哟个元素的值（也就是一行内容），然后将这一行内容赋值给line，
		// 接着line作为入参传递给line.split(" ").foreach(...)处理，将内容以空格切分成单词，单词有够长城一个集合
		// 然后又通过.foreach()对这个单词集合的每一个单词做处理
		listFiles.foreach((file: File) => Source.fromFile(file).getLines().foreach(line => line.split(" ").foreach(
				word => {
					if (wordsMap.contains(word)) {
						// 单词出现过，频次+1
						wordsMap(word) += 1
					} else {
						// 单词第一次出现创建一个(单词，频次)的Map条目
						wordsMap += (word -> 1)
					}
				}
			)
		  )
		)
		println(wordsMap)
		for ((key, value) <- wordsMap) {
			println(key + ": " + value)
		}
	}

}
