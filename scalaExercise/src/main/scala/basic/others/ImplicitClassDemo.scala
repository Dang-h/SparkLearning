package basic.others

object ImplicitClassDemo {
	def main(args: Array[String]): Unit = {
		implicit class DB(val mysql:Mysql){ // 隐式类，将MySQL转成DB
			def addSuffix(): String ={
				mysql + "Scala"
			}
		}

		val mysql = new Mysql
		mysql.sayOk()
		println(mysql.addSuffix())
	}

}

class Mysql{
	def sayOk(): Unit ={
		println("sayOK")
	}
}
