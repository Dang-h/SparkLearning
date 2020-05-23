# SparkLearning
spark学习，spark练习，spark项目实战

## SparkCore

### 知识点

- [算子的使用](sparkExercise/src/main/scala/sparkAnalyze/sparkCore/算子的使用.md) 

## Spark小练习
- [wordCount](sparkExercise/src/main/scala/sparkAnalyze/sparkCore/practice/WordCount.scala)

- [统计出每一个省份广告被点击次数的 TOP3](sparkExercise/src/main/scala/sparkAnalyze/sparkCore/practice/ClickTop3.scala)
```
数据结构：时间戳，省份，城市，用户，广告，中间字段使用空格分割。
		1516609143867 6 7 64 16
		1516609143869 9 4 75 18
		1516609143869 1 7 87 12
```
### 用户访问日志分析
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

### [成绩单分析]
数据格式：班级|姓名|年龄|性别|科目|成绩
题目：
- 读取文件的数据test.txt
- 一共有多少个小于20岁的人参加考试？
- 一共有多少个等于20岁的人参加考试？
- 一共有多少个大于20岁的人参加考试？
- 一共有多个男生参加考试？
- 一共有多少个女生参加考试？
- 12班有多少人参加考试？
- 13班有多少人参加考试？
- 语文科目的平均成绩是多少？
-  数学科目的平均成绩是多少？
-  英语科目的平均成绩是多少？
-  每个人平均成绩是多少？
-  12班平均成绩是多少？
-  12班男生平均总成绩是多少？
-  12班女生平均总成绩是多少？
-  13班平均成绩是多少？
-  13班男生平均总成绩是多少？
-  13班女生平均总成绩是多少？
-  全校语文成绩最高分是多少？
-  12班语文成绩最低分是多少？
-  13班数学最高成绩是多少？
-  总成绩大于150分的12班的女生有几个？
-  总成绩大于150分，且数学大于等于70，且年龄大于等于19岁的学生的平均成绩是多少？

### 评论日志分析
日志格式：

|字段名称|说明|
|:---|:---|
|index|数据Id|
|child_comment|回复数量|
|comment_time|评论时间|
|content|评论内容|
|da_v|微博个人认证|
|like_status|赞|
|pic|图片评论url|
|user_id|微博用户Id|
|user_name|微博用户名|
|vip_rank|微博会员等级|
|stamp|时间戳|

题目：
- 在kafka中创建rng_comment主题，设置2个分区2个副本
- [数据预处理，把空行和缺失字段的行过滤掉]
- [请把给出的文件写入到kafka中，根据数据id进行分区，id为奇数的发送到一个分区中，偶数的发送到另一个分区]
- ~~使用Spark Streaming对接kafka~~
- ~~使用Spark Streaming对接kafka之后进行计算~~
- 在mysql中创建一个数据库rng_comment
- 在数据库rng_comment创建vip_rank表，字段为数据的所有字段
- 在数据库rng_comment创建like_status表，字段为数据的所有字段
- 在数据库rng_comment创建count_comment表，字段为 时间，条数
- [查询出微博会员等级为5的用户，并把这些数据写入到mysql数据库中的vip_rank表中 ]
- [查询出评论赞的个数在10个以上的数据，并写入到mysql数据库中的like_status表中]
- [分别计算出2018/10/20 ，2018/10/21，2018/10/22，2018/10/23这四天每一天的评论数是多少，并写入到mysql数据库中的count_comment表中]
    