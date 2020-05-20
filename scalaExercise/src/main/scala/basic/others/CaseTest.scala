package basic.others

object CaseTest {
	def main(args: Array[String]): Unit = {
		val a = 7
		val obj = {
			if (a == 1) 1
			else if (a == 2) "2"
			else if (a == 3) BigInt(3)
			else if (a == 4) Map("aa" -> 1)
			else if (a == 5) Map(1 -> "aa")
			else if (a == 6) Array(1, 2, 3)
			else if (a == 7) Array("aa")
		}
		val result = obj match {
			case a: Int => a
//			case b: Map[String, Int] => "对象是一个字符串-数字的Map集合"
//			case c: Map[Int, String] => "对象是一个数字-字符串的Map集合"
			case d: Array[String] => "Array[String]"
			case e: BigInt => Int.MaxValue
			case _ => "Nothing"
		}
		println(result)
		println("==========================")

		for (arr <- Array(Array(0), Array(1, 0), Array(0, 1, 0),
			Array(0, 1, 0,1), Array(1, 1, 0, 1))) {
			val result = arr match {
				case Array(0) => "0"
				case Array(x, y) => x + "=" + y
				case Array(0, _*) => "以0开头和数组"
				case _ => "什么集合都不是"
			}
			println("result = " + result)
		}

		println("==========================")


		abstract class Amount
		case class Dollar(value: Double) extends Amount
		case class Currency(value: Double, unit: String) extends Amount
		case object NoAmount extends Amount

		for (amt <- Array(Dollar(1000.0), Currency(1000.0, "RMB"), NoAmount)) {
			val result = amt match {
				case Dollar(v) => "$" + v
				case Currency(v, u) => v + " " + u
				case NoAmount => ""
			}
			println(amt + ": " + result)
		}

		val amt = Currency(29.5,"RMB")
		val amt1 = amt.copy()
		val amt2 = amt.copy(value = 19.95)
		val amt3 = amt.copy(unit = "$")

		println(amt)
		println(amt1)
		println(amt2)
		println(amt3)

	}
}
