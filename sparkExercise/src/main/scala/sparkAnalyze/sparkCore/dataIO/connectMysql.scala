package sparkAnalyze.sparkCore.dataIO

import java.sql.{Connection, DriverManager, PreparedStatement}

import org.apache.spark.rdd.JdbcRDD
import org.apache.spark.{SparkConf, SparkContext}

// FIXME
object connectMysql {
	def main(args: Array[String]): Unit = {
		val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("connectMysql")
		val sc = new SparkContext(conf)

		val data = sc.parallelize(List((4, "test4"), (5, "test5")))

		// 定义MySQL参数
		val driver = "com.mysql.jdbc.Driver"
		val url = "jdbc:mysql://hadoop100:3306/test"
		val userName = "root"
		val password = "mysql"

		// 创建JdbcRDD
		val rdd = new JdbcRDD(sc, () => {
			Class.forName(driver)
			DriverManager.getConnection(url, userName, password)
		},
			"SELECT t.* FROM test.test t where `id` >= ? and `id` <= ?;", 1, 3, 1,
			r => (r.getInt(1), r.getString(2))
		)

		println(rdd.count())
		rdd.foreach(println)

		sc.stop()
	}

}
