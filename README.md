# SparkLearning
spark学习，spark练习，spark项目实战

## SparkCore

### 知识点

- [算子的使用](sparkExercise/src/main/scala/sparkAnalyze/sparkCore/算子的使用.md) 

### Spark小练习
- [wordCount](sparkExercise/src/main/scala/sparkAnalyze/sparkCore/practice/WordCount.scala)

- [统计出每一个省份广告被点击次数的 TOP3](sparkExercise/src/main/scala/sparkAnalyze/sparkCore/practice/ClickTop3.scala)
```
数据结构：时间戳，省份，城市，用户，广告，中间字段使用空格分割。
		1516609143867 6 7 64 16
		1516609143869 9 4 75 18
		1516609143869 1 7 87 12
```
#### 用户访问日志分析
日志格式：

|字段名称|说明|
|:---|:---|
|data|用户点击行为发生日期|
|user_id|用户ID|
|session_id|用户的一个访问session|
|page_id|页面ID，点击了商品嚯关键词进入的页面id|
|action_time|发生点击行为的时间点|
|search_keyword|搜索关键词|
|click_category_id|点击品类ID|
|click_product_id|点击的商品ID|
|order_category_ids|下单品类ID|
|order_product_ids|下单商品ID|
|pay_category_ids|付款品类ID|
|pay_product_ids|付款商品ID|
|city_id|城市ID|
需求：
- [取点击、下单和支付数量排名前 10 的品类并将计算结果存入MySQL](sparkExercise/src/main/scala/sparkAnalyze/sparkCore/practice/sparkMall/application/CategoryTop10App.scala)

- [取点击、下单和支付数量排名前 10 的品类(使用自定义累加器)](sparkExercise/src/main/scala/sparkAnalyze/sparkCore/practice/sparkMall/application/CategoryTop10App4Accumulator.scala)

- [Top10 品类中Top10 活跃的Session](sparkExercise/src/main/scala/sparkAnalyze/sparkCore/practice/sparkMall/application/Top10Category_SessionTop10.scala)

- [页面平均停留时间TOP10统计](sparkExercise/src/main/scala/sparkAnalyze/sparkCore/practice/sparkMall/application/PageAvgAccessTime.scala)

- [指定页面的单跳转率](sparkExercise/src/main/scala/sparkAnalyze/sparkCore/practice/sparkMall/application/PageFlow.scala)

