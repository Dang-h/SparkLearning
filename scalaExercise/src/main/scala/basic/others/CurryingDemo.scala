package basic.others

import java.io.{File, PrintWriter}

object CurryingDemo {
	// 比较两个字符串再忽略大小写的情况下是否相等
	// 简单方式,eqStr函数需要传入两个参数s1和s2
	def eqStr1(s1: String, s2: String) = {
		s1.toLowerCase == s2.toLowerCase
	}

	// 使用Curry化：接受多个参数的函数可以转换为接受单个参数的多个函数
	// 使用隐式类
	implicit class TestStrEq(s1: String) {
		def eqStr2(s2: String)(f: (String, String) => Boolean) = {
			f(s1.toLowerCase, s2.toLowerCase)
		}
	}

	// 调用：str1.eqStr2(str2)(_.equals(_))

	// 没有经过柯里化的函数，接收两个Int参数x和y做加法
	def sum(x: Int, y: Int) = x + y

	sum(1, 2)

	// 应用柯里化,实际上连着做了两次传统的函数调用，第一层次接受一个名为x的Int参数，返回一个用于第二次调用的函数值
	// 这个函数接收一个Int参数y
	def curriedSum(x: Int)(y: Int) = x + y

	curriedSum(1)(2)
	curriedSum(1) _

	def withPrintWriter(file: File, op: PrintWriter => Unit) = {
		val writer = new PrintWriter(file)
		try {
			op(writer)
		} finally {
			writer.close()
		}
	}

	def withPrintWriterWithCurry(file: File)(op: PrintWriter => Unit) = {
		val writer = new PrintWriter(file)
		try {
			op(writer)
		} finally {
			writer.close()
		}
	}

	def main(args: Array[String]): Unit = {
		// 只能用（）不能用{}
		withPrintWriter(
			new File("data.txt"),
			writer => writer.println(new java.util.Date())
		)
		val file = new File("date.txt")
		withPrintWriterWithCurry(file) {
			writer => writer.println(new java.util.Date)
		}
	}
}
