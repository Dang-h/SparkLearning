package sparkAnalyze.sparkCore.practice.sparkMall.common.model

/**
 * 用户访问动作表
 *
 * @param date               用户点击行为的日期
 * @param user_id            用户的 ID
 * @param session_id         Session 的 ID
 * @param page_id            某个页面的 ID
 * @param action_time        点击行为的时间点
 * @param search_keyword     用户搜索的关键词
 * @param click_category_id  某一个商品品类的 ID
 * @param click_product_id   某一个商品的 ID
 * @param order_category_ids 一次订单中所有品类的 ID 集合
 * @param order_product_ids  一次订单中所有商品的 ID 集合
 * @param pay_category_ids   一次支付中所有品类的 ID 集合
 * @param pay_product_ids    一次支付中所有商品的 ID 集合
 */
case class UserVisitAction(date: String,
						   user_id: String,
						   session_id: String,
						   page_id: Long,
						   action_time: String,
						   search_keyword: String,
						   click_category_id: Long,
						   click_product_id: Long,
						   order_category_ids: String,
						   order_product_ids: String,
						   pay_category_ids: String,
						   pay_product_ids: String,
						   city_id: String)

/**
 *
 * @param taskId		任务id
 * @param categoryId	Top10品类ID
 * @param clickCount	Top10点击次数
 * @param orderCount	Top10下单次数
 * @param payCount		Top10支付次数
 */
case class CategoryTop10(taskId: String,
						 categoryId: String,
						 clickCount: Long,
						 orderCount: Long,
						 payCount: Long)

case class CategoryTop10SessionTop10(taskId: String, categoryId: String, sessionId: String, clickCount: Long)