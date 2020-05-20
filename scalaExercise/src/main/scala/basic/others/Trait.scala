package basic.others

trait Flyable {
	var maxFlyHeight: Int

	def fly()

	def breathe(): Unit = {
		println("I can breathe")
	}
}

class Bird1(flyHeight: Int) extends Flyable {
	override var maxFlyHeight: Int = flyHeight

	// 重载特质的抽象方法
	override def fly(): Unit = {
		println(s"I can fly at the height of $flyHeight")
	}
}

class TestMatch {
	for (elem <- List(6, 9, 0.168, "Spark", "hadoop", "Hello")) {
		val str: String = elem match {
			case i: Int => i + " is an int value."
			case d: Double => d + " is a double value."
			case "Spark" => "Spark is found."
			case s: String => s + " is a string value."
			case _ => "unexpected value: " + elem
		}
		println(str)
	}
}


class TestMatch2 {
	for (elem <- List(1, 2, 3, 4)) {
		elem match {
			case _ if elem % 2 == 0 => println(elem + " is even")
			case _ => println(elem + " is odd.")
		}
	}
}

class TestMethod {
	def powerOfTwo(x: Int): Int = {
		if (x == 0) 1 else 2 * powerOfTwo(x - 1)
	}

	def sum(f: Int => Int, a: Int, b: Int): Int = {
		if (a > b) 0 else f(a) + sum(f, a + 1, b)
	}
}

class MethodExplore {
	val counter1: (Int) => Int = { value => value + 1 }
	// 此为标准定义：val/var 变量名：变量类型 = 初始值
	//其中，counter1 的变量类型为：（Int） => Int，它是一个函数
	//这个函数有一个参数（只有一个参数，括号可以省略），类型为Int；返回结果类型为Int的值
	//counter1: Int => Int = <function1>
	//定义一个名为counter1的函数，该函数有一个参数，类型为Int，返回结果类型为Int的值
	// counter1（1）结果为：Int=2
	val counter1_1: Int => Int = { value: Int => value + 1 }

	val counter2 = (value: Int) => value + 1
	// 若可以从最后一个表达式推测出结果的类型，变量类型可以省略
	// 只有一条语句大括号也可省略
	// 该函数中可有value+1推测出结果类型为Int，因为，前面定义了value为Int类型
	// 其中value:Int => value + 1 为匿名函数也成为函数的字面量（直接在源代码中书写常量的一种方式）
	// 参数列表=>函数体
	val counter2_1: Int => Int = (value: Int) => value + 1

	val counter3 = (_: Int) + 1
	// 当函数的每个参数在函数字面量内仅出现一次，可以省略=>并用下划线作为参数的占位符来简化表示
	// value:Int => value + 1中，参数value，仅出现了一次，省略=>并用_代替value
	// 得到：_:Int + 1
	val counter3_1: Int => Int = (_: Int) + 1
	// Int => Int 表示：这是一个函数，函数有一个参数，类型为Int，函数的结果返回类型为Int的值

	// 高阶函数：参数含有函数或者赶回结果为函数的方法
	def sum(f: Int => Int, a: Int, b: Int): Int = {
		if (a > b) 0 else f(a) + sum(f, a + 1, b)
	}

	// sum: (f: Int => Int, a: Int, b: Int)Int
	// 定义一个名为sum的方法，该方法有三个参数，函数f，a和b
	// 参数1是一个名为f的函数，该函数有一个Int类型的参数，返回值类型为Int
	// 调用的时候，参数1可以传入一个函数，如：x=> x*x
	// 可以通过_将一个方法转换为函数，表示保留整个参数列表
	val methodOfSum = sum _
	val methodOfSum1: (Int => Int, Int, Int) => Int = sum
	// val 变量名：变量类型 = 初始值

	//call-by-name和call-by-value
	//call-by-name：传名调用，在参数名称和参数类型之间有=>符号，将为计算的参数表达式直接应用到函数内部
	def addByName(a: Int, b: => Int) = a + b

	// call-by-value:传值调用，先计算参数表达式的值在应用到函数内部
	def addByValue(a: Int, b: Int) = a + b

	def invoke(f: String => Int => Long) = {
		println("call invoke")
		f("1")(2)
	}

	// 定义一个名为invoke的方法，该方法有一个参数，这个参数是一个函数
	// TODO ??? f:String => Int => Long

	//偏应用函数：只保留函数部分参数的表达式

}


import scala.util.control.Breaks._

class TestBreakable {
	var n = 1
	// break
	breakable {
		while (n <= 20) {
			n += 1
			println("n=" + n)
			if (n == 18) {
				break()
			}
		}
	}

	// continue
	for (i <- 1 to 10 if i != 2 && i != 3) {
		println("i= " + i)
	}
	for (i <- 1 to 10) {
		breakable {
			if (i == 2) break()
			else if (i == 3) break()
			else println("i = " + i)
		}
	}
}

object Trait {
	def main(args: Array[String]): Unit = {
		//		val testMatch = new TestMatch
		//		val testMatch = new TestMatch2
		val method = new TestMethod
		println(method.sum(x => x, 1, 5))
	}
}

object ConstructorDemo {

	def main(args: Array[String]): Unit = {
		val p1 = new Person("Tom", 10)
		val p2 = new Person("Tom")
		//		println(p1)
		println(p2)
	}
}

class Person(inName: String, inAge: Int) {


	def this(name: String) {
		// 辅助构造器，必须在第一行显示调用主构造器
		this("jack", 10)
		this.name = name
	}

	var name: String = inName
	var age: Int = inAge
	age += 10
	println("~~~~~~~~~")

	override def toString: String = "name=" + this.name + "\t age = " + age

}

class Counter {

	private var value = 0
	private var name = ""
	private var step = 1
	println("the main constructor")

	def this(name: String) {
		this()
		this.name = name
		println(s"the first auxiliary constructor, name:$name")
	}

	def this(name: String, step: Int) {
		this(name) // 调用第一个auxiliary constructor
		this.step = step
		println(s"the second auxiliary constructor, name:$name")
	}

	def increment(step: Int) = {
		value += step
	}

	def current(): Int = value
}

object Counter {
	def main(args: Array[String]): Unit = {
		val c1 = new Counter()
		println(c1)
		val c2 = new Counter("the 2nd Counter")
		println(c2)
		val c3 = new Counter("the 3nd Counter", 2)
		println(c3)
	}
}

import scala.beans.BeanProperty

object BeanPropertyDemo {
	def main(args: Array[String]): Unit = {

		val car = new Car()
		car.name = "BMW"
		println(car.name)

		car.setName("Benz")
		println(car.getName)
	}
}

class Car() {
	@BeanProperty var name: String = _

}

object BankDemo {
	def main(args: Array[String]): Unit = {
		val account = new Account("1111111", 890.123, "111111")
		account.query("111111")
		account.withDraw("111111", 100)
		account.query("111111")

	}
}

class Account(inAccount: String, inBalance: Double, inPwd: String) {
	val accountNo = inAccount
	var balance = inBalance
	val pwd = inPwd

	def query(pwd: String): Unit = {
		if (!this.pwd.equals(pwd)) {
			println("Password Error!")
			return
		}
		printf("Account:%s, balance:%.2f\n", accountNo, balance)
	}

	def withDraw(pwd: String, money: Double): Any = {
		if (!this.pwd.equals(pwd)) {
			println("Password Error!")
			return
		}

		if (this.balance < money) {
			println("余额不足not sufficient money")
			return
		}

		this.balance -= money
		money
	}

	def sum(n1: Int, args: Int*): Int = {
		println("args.length" + args.length)
		var sum = n1
		for (item <- args) sum += item
		sum
	}
}

class Base {
	var n1: Int = 1
	protected var n2: Int = 2
	private var n3: Int = 3

	def test100() = {
		println("base 100")
	}

	protected def test200(): Unit = {
		println("base 200")
	}

	private def test300(): Unit = {
		println("base300")
	}
}

class Sub extends Base {
	def sayOk: Unit = {
		this.n1 = 20
		this.n2 = 40
		println("range:" + this.n1 + this.n2)

		test100()
		test200()
//		test300()
	}
}
object TestExtend{
	def main(args: Array[String]): Unit = {
		val sub = new Sub()
//		sub.sayOk
	}
}