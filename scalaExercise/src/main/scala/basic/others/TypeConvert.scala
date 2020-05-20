package basic.others

object TypeConvert {
	def main(args: Array[String]): Unit = {
		// ClassOf
		println(classOf[String])

		val s ="king"
		println(s.getClass.getName)// 使用了反射机制

		var p1 = new Person200
		var emp =  new Emp200

		p1 = emp
		val bool: Boolean = p1.isInstanceOf[Emp200]
		var emp2 = p1.asInstanceOf[Emp200]// 将
		emp2.sayHi
	}

}

class Person200{
	var name = "Tom"

	def printName(): Unit ={
		println("Person printName()" + name)
	}

	def sayHi: Unit ={
		println("Person200 sayHi....")
	}
}

class Emp200 extends Person200 {
	override def printName(): Unit = {
		println("Emp printName()" + name)
		super.printName()
		sayHi
	}

	def sayHello(): Unit ={
		println("Emp200 sayHello()")
	}

	
}
