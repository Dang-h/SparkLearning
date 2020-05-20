package basic.others

class ByNameParamDemo {
	// 不适用传名参数
	var assertionsEnabled = true

	def myAssert(predicate: () => Boolean) =
		if (assertionsEnabled && !predicate())
			throw new AssertionError

	// 调用需要带上（）=>
	myAssert(() => 5 > 3)

	// 传名参数由此诞生;将myAssert的predicate参数转成传名参数：（）=>Boolean改成=>Boolean
	def byNameAssert(predicate: => Boolean) =
		if (assertionsEnabled && !predicate)
			throw new AssertionError

	byNameAssert(5 > 3)

}
