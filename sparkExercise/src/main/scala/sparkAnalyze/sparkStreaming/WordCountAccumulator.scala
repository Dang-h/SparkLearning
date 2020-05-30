package sparkAnalyze.sparkStreaming

import org.apache.spark.SparkContext
import org.apache.spark.util.AccumulatorV2

import scala.collection.mutable

// 自定义累加器
// 两个泛型分别存储的是单词和单词总数的映射
class WordCountAccumulator[T] private extends AccumulatorV2[T, mutable.Map[String, Int]] {

	// 用于存储累加器中不断累加的数据
	private val _map: mutable.Map[String, Int] = mutable.Map()

	// 判断累加器中的数据是否为初始状态
	override def isZero: Boolean = _map.isEmpty

	// 将当前累加器及其内部的数据复制到另一个新的累加器中
	override def copy(): AccumulatorV2[T, mutable.Map[String, Int]] = {
		val newACC = new WordCountAccumulator[T]
		for (elem <- _map) {
			newACC._map += elem
		}
		newACC
	}

	// 重置累加器中的数据
	override def reset(): Unit = {
		_map.clear()
	}

	// 在累加器中累加单词个数
	override def add(v: T): Unit = {
		val wordMapping: (String, Int) = v.asInstanceOf[(String, Int)]
		_map += Tuple2(wordMapping._1, wordMapping._2 + _map.getOrElse(wordMapping._1, 0))
	}

	// 将每个分区中的累加器合并到一个累加器中
	override def merge(other: AccumulatorV2[T, mutable.Map[String, Int]]): Unit = {
		val _other: WordCountAccumulator[T] = other.asInstanceOf[WordCountAccumulator[T]]
		for ((x, y) <- _map) {
			_map += Tuple2(x, y + _other._map.getOrElse(x, 0))
			_other._map.remove(x)
		}

		for (elem <- _other._map) {
			_map += elem
		}
	}

	// 返回累加器中的值
	override def value: mutable.Map[String, Int] = _map
}

// 涉及单例模式实现累加器初始化
object WordCountAccumulator {
	// 使用@volatile申明变量主要是为了确保多线程更新共享变量状态的及时性
	@volatile private var instance: WordCountAccumulator[(String, Int)] = null

	// 此处实现了多线程下的”双重检查锁“的单例模式
	def getInstance(sc: SparkContext): WordCountAccumulator[(String, Int)] = {
		if (instance == null) {
			synchronized {
				if (instance == null) {
					instance = new WordCountAccumulator[(String, Int)]()
				}
			}
		}

		if (!instance.isRegistered) sc.register(instance)

		instance
	}
}