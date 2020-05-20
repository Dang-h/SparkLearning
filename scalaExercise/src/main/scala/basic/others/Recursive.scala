package basic.others

object Recursive {
	def main(args: Array[String]): Unit = {
		//		println("fibonacci1: " + fibonacci1(5))
		//		println("fibonacci2: " + fibonacci2(5))

		//		println(peach(1))
		//		println(max(List(2, 1, 3, 1, 23, 12, 32)))
		println(reverse("abcdefg"))
		println(factorial(3))
	}

	def fibonacci1(n: Int): Int = {
		if (n == 1 || n == 2) {
			println("n=1||n=2 " + n)
			return 1
		} else {
			println("n= " + n)
			return fibonacci1(n - 1) + fibonacci1(n - 2)
		}
	}

	def fibonacci2(n: Int): Int = {
		if (n == 1) {
			println("n==1 " + n)
			return 3
		} else {
			println("n= " + n)
			return 2 * fibonacci2(n - 1) + 1
		}
	}

	//猴子吃桃子问题：有一堆桃子，猴子第一天吃了其中的一半，并再多吃了一个！以后每天猴子都吃其中的一半，然后再多吃一个。当到第十天时，想再吃时（还没吃），发现只有1个桃子了。问题：最初共多少个桃子？
	// 逆推：每天吃其中一半再多吃一个；第10天只剩下1个，设第9天有x个桃，x-(x/2+1)=1 得x=4(即：(1+1)*2=4)；
	// 第8天则有（4+1）*2=10个桃；第7天则有（10+1）*2=22。。。（昨天剩下的桃+1）*2=今天的桃

	def peach(n: Int): Int = {
		if (n == 10) {
			return 1
		} else {
			return (peach(n + 1) + 1) * 2
		}
	}

	// 求最大值
	def max(list: List[Int]): Int = {
		if (list.isEmpty) {
			throw new java.util.NoSuchElementException
		}
		if (list.size == 1) {
			list.head
		} else if (list.head > max(list.tail)) {
			list.head
		} else {
			max(list.tail)
		}
	}

	// 反转字符串
	def reverse(str: String): String = {
		if (str.length == 1) str else reverse(str.tail) + str.head
	}

	// 求阶乘
	def factorial(n: Int): Int = {
		if (n == 0) 1 else n * factorial(n - 1)
	}
}
