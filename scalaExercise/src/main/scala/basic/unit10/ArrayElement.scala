package basic.unit10

/*
// 存在代码冗余：conts和contents
class ArrayElement(conts: Array[String]) extends Element {
	// contents在父类Element中是抽象的，在这里，我们说ArrayElement里contents方法实现了Element类的抽象方法
	def contents: Array[String] = conts
	// 重写
//	val contents:Array[String] = conts
}
 */
// 漂亮打开方式，但是contents字段不能被从新赋值因为它是val
//class ArrayElement(val contents: Array[String]) extends Element
// 完美解决
class ArrayElement(param: Array[String]) extends Element {
	val contents: Array[String] = param
}