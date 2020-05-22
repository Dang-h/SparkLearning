package sparkAnalyze.sparkCore.practice.sparkMall.common.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.logging.SimpleFormatter

object DateParse {
	/**
	 * 将时间字符串按特定格式解析成时间
	 *
	 * @param s 待解析日期字符串
	 * @param f 解析格式（默认：yyyy-MM-dd HH:mm:ss）
	 * @return
	 */
	def parseString2Date(s: String, f: String = "yyyy-MM-dd HH:mm:ss"): Date = {
		val formatter = new SimpleDateFormat(f)
		formatter.parse(s)
	}

	/**
	 * 将时间字符串按特定格式解析成时间戳
	 *
	 * @param s 待解析日期字符串
	 * @param f 解析格式（默认：yyyy-MM-dd HH:mm:ss）
	 * @return
	 */
	def parseString2Timestamp(s: String, f: String = "yyyy-MM-dd HH:mm:ss"): Long = {
		parseString2Date(s, f).getTime
	}
}
