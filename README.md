# SparkLearning
spark学习，spark练习，spark项目实战

## 知识点
### SparkCore
- [算子的使用](sparkExercise/src/main/scala/sparkAnalyze/sparkCore/算子的使用.md) 

### SparkSQL
- [DataFrame和Dataset的相互转换、创建及数据输出](sparkExercise/src/main/scala/sparkAnalyze/sparkSQL/CreateDF_DS.scala)
    - 通过序列集合创建DataFrame
    - 自定义schema创建DataFrame
    - 读取MySQL中的数据到DataFrame
    - 输出数据到MySQL
- ["一进一出"-UDF](sparkExercise/src/main/scala/sparkAnalyze/sparkSQL/UDF.scala)
- ["一进多出"-UDTF](sparkExercise/src/main/scala/sparkAnalyze/sparkSQL/UDTF.scala)
- ["多进一出"-UDAF](sparkExercise/src/main/scala/sparkAnalyze/sparkSQL/UDAF.scala)

## Spark小练习
- [wordCount](sparkExercise/src/main/scala/sparkAnalyze/sparkCore/practice/WordCount.scala)

***
- [统计出每一个省份广告被点击次数的 TOP3](sparkExercise/src/main/scala/sparkAnalyze/sparkCore/practice/ClickTop3.scala)

[数据格式](data/practice/agent.log)

时间戳，省份，城市，用户，广告(中间字段使用空格分割)。
***
### [成绩单分析](sparkExercise/src/main/scala/sparkAnalyze/sparkCore/practice/Report.scala)

[数据格式](data/practice/data.txt)

姓名、课程、分数

需求：
- 求每名同学选修的课程数
- 求各门课程的平均分
- 使用累加器求DataBase课程的选修人数
***
### 用户访问日志分析
[日志格式](data/project/user_visit_action.csv)

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
***
### [订单分析](sparkExercise/src/main/scala/sparkAnalyze/sparkSQL/orderAnalyze/sql.scala)
数据格式

[tbDate](data/practice/tbDate.txt)

|字段|类型|说明|
|:---|:---|:---|
|dateid|date|日期|
|years|varchar|年月|
|theyear|varchar|年|
|month|varchar|月|
|day|varchar|日|
|weekday|varchar|周几|
|week|varchar|第几周|
|quarter|varchar|季度|
|period|varchar|旬|
|halfmonth|varchar|半月|

[tbStockDetail](data/practice/tbStockDetail.txt)

|字段|类型|说明|
|:---|:---|:---|
|ordernumber|varchar|订单号|
|rownum|varchar|行号|
|itemid|varchar|货号|
|number|varchar|数量|
|price|varchar|单价|
|amount|int|销售额|

[tbStock](data/practice/tbStock.txt)

|字段|类型|说明|
|:---|:---|:---|
|ordernumber|varchar|订单号|
|locationid|varchar|交易位置|
|dateid|date|交易日期|

需求：
- 计算所有订单中每年的销售单数、销售总额
- 计算所有订单每年最大金额订单的销售额
- 计算所有订单中每年最畅销货品
***
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
    