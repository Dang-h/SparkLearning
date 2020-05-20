package basic.others

object ImplicitDemo {
	def main(args: Array[String]): Unit = {
		implicit val name = "Scala"

		// 声明一个函数，函数有一个隐式参数
		def hello(implicit content: String): Unit = {
			println("Hello " + content)
		}

		// 调用hello,不传参数，它会默认调用声明的隐式值
		hello
//		hello() 使用了隐式参数调用时不传参不能带（）
		hello("Spark")
	}

	def hello(): Unit = {
		println("hello()")
	}
}
