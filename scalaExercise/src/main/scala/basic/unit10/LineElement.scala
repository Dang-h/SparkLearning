package basic.unit10

// ArrayElement的构造方法接收一个参数（Array[String]）
// LineElement需要想它的父类的主构造方法中传入一个同类型的入参
//class LineElement(s: String) extends ArrayElement(Array(s)) {
class LineElement(s: String) extends Element {
	val contents = Array(s)
	/**
	 *
	 * @return contents中的行数
	 */
	override def height: Int = 1
	/**
	 *
	 * @return 第一行的长度，没有则返回0
	 */
	override def width: Int = s.length
}
