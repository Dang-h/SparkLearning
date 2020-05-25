package sparkAnalyze.sparkSQL.orderAnalyze

// spark中在driver端实例化的类必须继承Serializable，而executor端的类没有此要求（driver端需要通过网络发到executor中）
case class tbStock(ordernumber: String, locationid: String, dateid: String) extends Serializable

case class tbStockDetail(ordernumber: String, rownum: Int, itemid: String, number: Int, price: Double,
						 amount: Double) extends Serializable

case class tbDate(dateid: String, years: Int, theyear: Int, month: Int, day: Int, weekday: Int, week: Int,
				  quarter: Int, period: Int, halfmonth: Int) extends Serializable