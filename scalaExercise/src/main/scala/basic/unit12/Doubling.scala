package basic.unit12

// 声明了一个超类IntQueue，意味着这个特质只能被混入同样继承自IntQueue的类（比如：BasicIntQueue）
trait Doubling extends IntQueue {
	abstract override def put(x: Int) = {
		super.put(2 * x)
	}
}
