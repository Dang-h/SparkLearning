package basic.others

import java.io.File

/**
 * 闭包的应用
 */
object ClosureDemo {
	private def filesHere = (new File(".")).listFiles

	/**
	 * 用于查询指定扩展名结尾的文件
	 *
	 * @param query 指定扩展名
	 * @return 文件序列
	 */
	//	def filesEnding(query: String) =
	//		for (file <- filesHere; if file.getName.endsWith(query)) yield file
	//	def filesEnding(query: String) =
	//		filesMatcher(query, _.endsWith(_))
	def filesEnding(query: String) =
		filesMatcher(_.endsWith(query))

	//	def filesContaining(query: String) =
	//		for (file <- filesHere; if file.getName.contains(query)) yield file
	//	def filesContaining(query: String) =
	//		filesMatcher(query, _.contains(_))
	def filesContaining(query: String) =
		for (file <- filesMatcher(_.contains(query))) yield file

	//	def filesRegex(query: String) =
	//		for (file <- filesHere; if file.getName.matches(query)) yield file
	//	def filesRegex(query: String) =
	//		filesMatcher(query, _.matches(_))
	def filesRegex(query: String) =
		for (file <- filesMatcher(_.matches(query))) yield file

	//	def filesMatcher(query: String, matcher: (String, String) => Boolean) = {
	//		for (file <- filesHere; if matcher(file.getName, query)) yield file
	//	}
	def filesMatcher(matcher: String => Boolean) =
		for (file <- filesHere; if matcher(file.getName)) yield file

	/**
	 * 判定传入的List是否包含负数
	 *
	 * @param nums 待检查的List
	 * @return true - 包含； false - 不包含
	 */
	//	def containsNeg(nums: List[Int]): Boolean = {
	//		var exists = false
	//		for (num <- nums)
	//			if (num < 0)
	//				exists = true
	//		exists
	//	}
	def containsNeg(nums: List[Int]) = nums.exists(_ < 0)
}
