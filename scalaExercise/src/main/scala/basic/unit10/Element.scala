package basic.unit10

// Element类型表示元素；元素是一个有字符串组成的二维矩阵，用一个成员contents表示某个布局元素的内容
// 内容用字符串的数组表示，每个字符串代表一行

import Element.elem

abstract class Element {
	def contents: Array[String]
	// 定义无参方法
	/**
	 *
	 * @return contents中的行数
	 */
	def height: Int = contents.length
	/**
	 *
	 * @return 第一行的长度，没有则返回0
	 */
	def width: Int = if (height == 0) 0 else contents(0).length
	//	def above(that: Element): Element = new ArrayElement(this.contents ++ that.contents)
	def above(that: Element): Element = {
		val this1 = this widen that.width
		val that1 = that widen this.width
		// 加上断言确保被加宽的元素具有相同宽度
		assert(this1.width == that1.width)
		elem(this1.contents ++ that1.contents)
	}
	//	def beside(that: Element): Element = new ArrayElement(
	//		for ((line1, line2) <- this.contents zip that.contents) yield line1 + line2
	//	)
	def beside(that: Element): Element = {
		val this1 = this heighten that.height
		val that1 = that heighten this.height
		elem(for ((line1, line2) <- this1.contents zip that1.contents) yield line1 + line2)
	}
	def widen(w: Int) = {
		if (w <= width) {
			this
		} else {
			val left = elem(' ', (w - width) / 2, height)
			val right = elem(' ', w - width - left.width, height)
			left beside this beside right
		}
	}
	def heighten(h: Int): Element = {
		if (h <= height) {
			this
		} else {
			val top = elem(' ', width, (h - height) / 2)
			val bot = elem(' ', width, h - height - top.height)
			top above this above bot
		}
	}

	override def toString: String = contents mkString "\n"
}

// 带有工厂方法的工厂对象
object Element {

	private class ArrayElement(val contents: Array[String]) extends Element

	private class LineElement(s: String) extends Element {
		val contents = Array(s)
		override def width: Int = s.length
		override def height: Int = 1
	}

	private class UniformElement(ch: Char, override val width: Int, override val height: Int) extends Element {
		private val line = ch.toString * width
		def contents = Array.fill(height)(line)
	}

	def elem(contents: Array[String]): Element = new ArrayElement(contents)
	def elem(chr: Char, width: Int, height: Int): Element = new UniformElement(chr, width, height)
	def elem(line: String): Element = new LineElement(line)
}
