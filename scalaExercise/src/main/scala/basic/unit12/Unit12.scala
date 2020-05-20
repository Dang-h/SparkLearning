package basic.unit12

object Unit12 {
	def main(args: Array[String]): Unit = {
		val queue = new BasicIntQueue with Incrementing with Doubling
		queue.put(42)

	}
}
