package sparkAnalyze.sparkCore.practice.sparkMall.common.utils

object StrUtil {
	def isNotEmpty(str: String): Boolean = {
		str != null && str != "".equals(str.trim)
	}
}
