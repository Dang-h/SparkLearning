package project.p1

import java.io.File

import scala.collection.immutable
import scala.io.{BufferedSource, Source}

object SchoolReport {
	def main(args: Array[String]): Unit = {
		// 导入文件数据
		// 对数据进行切分，并将数据封装到容器（collection）中；由于数据可能涉及多次遍历，可将其封装成List
		//		val sourceDir = new File("D:\\Develop\\Coding\\sparkExercise\\data\\project")
		//		val filesList: List[File] = sourceDir.listFiles.toList
		//		filesList.foreach(file => Source.fromFile(file).getLines.map(_.split("\\s+")).toList.foreach(
		//			data =>
		//		))
		val inputFile: BufferedSource = Source.fromFile("D:\\Develop\\Coding\\sparkExercise\\data\\project\\schoolReport1.txt")
		val oriData: List[Array[String]] = inputFile.getLines.map(_.split("\\s+")).toList

		val courseNames: Array[String] = oriData.head.drop(2) // 获取课程名称
		val allStudents: List[Array[String]] = oriData.tail // 获取所有学生
		val courseNum: Int = courseNames.length // 获取课程数量用于求平均分

		/**
		 * 统计函数
		 *
		 * @param lines 需要统计的行
		 * @return 课程的平均分，最低分，最高分
		 */
		def statistic(lines: List[Array[String]]) = {
			(for (i <- 2 to courseNum + 1) yield {
				val temp: List[Double] = lines.map(elem => elem(i).toDouble)
				(temp.sum, temp.min, temp.max)
			}).map {
				case (total, min, max) => (total / lines.length, min, max)
			}
		}

		def printResult(result: Seq[(Double, Double, Double)]): Unit = {
			// 用zip将课程容器和结果容器合并-->（课程， 分数）
			(courseNames.zip(result)).foreach {
				case (course, score) => println(f"${course + ":"}%-10s${score._1}%5.2f${score._2}%8.2f${score._3}%8.2f")
			}
		}

		// 调用函数进行数据统计
		val allResult: immutable.IndexedSeq[(Double, Double, Double)] = statistic(allStudents)
//		println("course    average   min   max")
//		printResult(allResult)

		// 按性别统计
		val (maleLines, femaleLines) = allStudents.partition((_: Array[String]) (1) == "male")

		// 分别调用两个函数统计
		val maleResult = statistic(maleLines)
		println("course    average   min   max  [male]")
		printResult(maleResult)

		val femaleResult = statistic(femaleLines)
//		println("course    average   min   max  [female]")
//		printResult(femaleResult)

	}

}
