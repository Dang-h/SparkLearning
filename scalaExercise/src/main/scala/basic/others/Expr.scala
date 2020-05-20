package basic.others

import java.io.File

abstract class Expr

case class Var(name: String) extends Expr

case class Number(num: Double) extends Expr

case class UnOp(operator: String, arg: Expr) extends Expr

case class BinOp(operator: String, left: Expr, right: Expr) extends Expr


class Rational(n: Int, d: Int) {
	require(d != 0)

	private val g = gcd(n.abs, d.abs)
	val numer: Int = n / g
	val denom: Int = d / g

	// 辅助构造器
	def this(n: Int) = this(n, 1)

	override def toString: String = numer + "/" + denom

	// 最大公约数（greatest common divisor）
	@scala.annotation.tailrec
	private def gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)

	def +(that: Rational): Rational =
		new Rational(
			numer * that.denom + that.numer * denom, denom * that.denom
		)

	def +(i: Int): Rational = new Rational(numer + i * denom, denom)

	def *(that: Rational) = new Rational(numer * that.numer, denom * that.denom)

	def lessThan(that: Rational) =
		numer * that.denom < that.numer * denom

	def max(that: Rational) = if (this.lessThan(that)) that else this
}

object Test {
	def main(args: Array[String]): Unit = {
		val rational = new Rational(1, 2)
		val x = new Rational(1, 2)
		val y = new Rational(2, 3)

		// 高级语言功能：隐式转换
		// 2 + x 等价于2.(x),对整数2调用Rational方法，可Int没这个方法，使用隐式转换，将Int转成Rational类
		implicit def int2Rational(x: Int): Rational = new Rational(x)

		//		println(2 + x)
		//		println(x + 2)
		//		println(rational.lessThan(new Rational(3, 4)))
		//		println(rational.max(new Rational(2, 2)))

		def gcdLoop(x: Long, y: Long): Long = {
			var a = x
			var b = y
			while (a != 0) {
				val temp = a
				a = b % a
				b = temp
			}
			b
		}

		//		gcdLoop(2, 13)

		def fileHere = (new File("D:\\Develop\\Coding\\sparkExercise\\scalaExercise\\src\\main\\scala\\basic\\unit3")).listFiles()

		def fileLines(file: java.io.File): List[String] = scala.io.Source.fromFile(file).getLines().toList

		def grep(pattern: String) = {
			for (
				file <- fileHere
				if file.getName.endsWith(".scala");
				line <- fileLines(file)
				if line.trim.matches(pattern)
			) println(file + " : " + line.trim)
		}

		def scalaFiles: Array[File] = for (file <- fileHere if file.getName.endsWith(".scala")) yield file

		//		fileHere.foreach(println)
		//		for (file <- fileHere if file.getName.endsWith(".scala"); line <- fileLines(file)) println(line)
		//		grep(".*widthOgLength.*")

		def makeRowSeq(row: Int): Seq[String] = {
			for (col <- 1 to 10) yield {
				val prod = (row * col).toString // 计算每一行的值
				val padding = " " * (4 - prod.length) // 为了格式对齐填补空格
				padding + prod
			}
		}

		def makRow(row: Int) = makeRowSeq(row).mkString

		def multiTable() = {
			val tableSeq = for (row <- 1 to 10) yield makRow(row)
			tableSeq.mkString("\n")
		}

		print(multiTable())
	}
}