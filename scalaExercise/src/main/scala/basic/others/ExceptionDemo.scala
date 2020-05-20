package basic.others

object ExceptionDemo {
	def main(args: Array[String]): Unit = {
		f()
		println("throws test")
	}

	@throws(classOf[NumberFormatException])
	def f() = {
		"abc".toDouble
	}
}
