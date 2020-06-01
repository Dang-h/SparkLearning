package sparkAnalyze.sparkStreaming.dsTreamOpration

import java.sql.{Connection, DriverManager, PreparedStatement}
import java.util.concurrent.ConcurrentLinkedDeque

import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

// 输出文件到MySQL
object ForeachRDD {
	def main(args: Array[String]): Unit = {
		val sc = new SparkContext(new SparkConf().setMaster("local[*]").setAppName("output file to MySQL"))
		val ssc = new StreamingContext(sc, Seconds(5))

		val lineDStream: ReceiverInputDStream[String] = ssc.socketTextStream("hadoop100", 9999)
		val resultDStream: DStream[Array[String]] = lineDStream.map(_.split(",")).filter(_.length == 4)

		resultDStream.foreachRDD(rdd => {
			// 在Executor中执行
			rdd.foreachPartition(p => {
				val conn: Connection = ConnectionPool.getConnection()
				val statement: PreparedStatement = conn.prepareStatement(
					"INSERT INTO syllabus.t_car_position (`plate_num`, `longitude`, `latitude`, `timestamp`) VALUES (?, " +
					  "?, ?, ?);")
				// Executor中执行
				p.foreach(result => {
					statement.setString(1, result(0))
					statement.setFloat(2, result(1).toFloat)
					statement.setFloat(3, result(2).toFloat)
					statement.setLong(4, result(3).toLong)
					statement.addBatch()
				})
				statement.executeBatch()
				conn.commit()
				ConnectionPool.returnConnection(conn)
			})
		})
	}
}

// 创建连接池工具类
object ConnectionPool {

	// ConcurrentLinkedDeque是Java提供的无界非阻塞队列
	private var queue: ConcurrentLinkedDeque[Connection] = _

	Class.forName("com.mysql.jdbc.Driver")
	def getConnection() = {
		if (queue == null) queue = new ConcurrentLinkedDeque[Connection]()
		if (queue.isEmpty) {
			// 设置每个worker中连接池维护的连接数，取决于Worker节点的CPu核心数
			for (i <- 1 to 2) {
				val conn = DriverManager.getConnection(
					"jdbc:mysql://hadoop100:3306/sparkExercise?useUnicode=true&characterEncoding=utf8",
					"root",
					"mysql"
				)
				conn.setAutoCommit(false)
				queue.offer(conn)
			}
		}

		queue.poll()
	}

	def returnConnection(conn: Connection) = {
		queue.offer(conn)
	}
}
