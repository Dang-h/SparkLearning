package basic.others

object Symbol {
	def main(args: Array[String]): Unit = {
		biology(Seq(new Bird, new Dog, new Animal))
	}
	// <:为Scala泛型中的上边界，T<:Animal表示T必须是Animal的子类（Bird,Dog）
	// 此处定义了一个名为biology的函数，函数需要传入一个参数thing，参数的类型是一个集合，集合的类型必须是Animal及它的子类，然后对传入的元素调用sound方法
	def biology[T <: Animal](thing: Seq[T]) = thing.map(_.sound)
}

class Earth {
	def sound(): Unit = {
		println("hello!")
	}
}

class Animal extends Earth {
	override def sound(): Unit = {
		println("animal sound!")
	}
}

class Bird extends Animal {
	override def sound(): Unit = {
		println("bird sound!")
	}
}

class Dog extends Animal {
	override def sound(): Unit = {
		println("Dog sound!")
	}
}
