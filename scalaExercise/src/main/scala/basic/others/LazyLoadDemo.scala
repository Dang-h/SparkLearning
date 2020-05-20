package basic.others

object LazyLoadDemo {

	def sum(n1: Int, n2: Int) = {
		println("sum Loaded")
		println(n1 + n2)
	}

	def main(args: Array[String]): Unit = {
		// lazy不能修饰var
		lazy val res = sum(10, 20)
		println("--------------")
		println("res = " + res)
	}
}
