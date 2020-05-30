# SparkLearning
spark学习，spark练习，spark项目实战

## 知识点
### SparkCore
- [算子的使用](sparkExercise/src/main/scala/sparkAnalyze/sparkCore/算子的使用.md) 
- 累加器
    > 一种共享变量机制;每一个task会被分配到不同的节点（进程）中执行，可通过累加器将多个节点中的数据累加到同一变量中
    - [longAccumulator](sparkExercise/src/main/scala/sparkAnalyze/sparkCore/rdd4Advance/AccumulatorTest1.scala):长整、双精度浮点累加器
    - [collectionAccumulator](sparkExercise/src/main/scala/sparkAnalyze/sparkCore/rdd4Advance/AccumulatorTest2.scala):集合累加器
    - [自定义累加器](sparkExercise/src/main/scala/sparkAnalyze/sparkCore/rdd4Advance/AccumulatorCustomer.scala)

### SparkSQL
- [DataFrame和Dataset的相互转换、创建及数据输出](sparkExercise/src/main/scala/sparkAnalyze/sparkSQL/CreateDF_DS.scala)
    - 通过序列集合创建DataFrame
    - 自定义schema创建DataFrame
    - 读取MySQL中的数据到DataFrame
    - 输出数据到MySQL
- ["一进一出"-UDF](sparkExercise/src/main/scala/sparkAnalyze/sparkSQL/UDF.scala)
- ["一进多出"-UDTF](sparkExercise/src/main/scala/sparkAnalyze/sparkSQL/UDTF.scala)
- ["多进一出"-UDAF](sparkExercise/src/main/scala/sparkAnalyze/sparkSQL/UDAF.scala)

### SparkStreaming

- 读取数据到DStream
    - [读取HDFS文件夹中的数据](sparkExercise/src/main/scala/sparkAnalyze/sparkStreaming/readFile/ReadHDFSFile.scala)
    - [读取RDD组成的数据队列](sparkExercise/src/main/scala/sparkAnalyze/sparkStreaming/readFile/ReadRDD.scala)
    - ~~读取Flume中的数据~~
    - [读取Kafka中的数据`自动维护偏移量`](sparkExercise/src/main/scala/sparkAnalyze/sparkStreaming/readFile/ReadKafka.scala)
    - [**读取Kafka中数据** `手动维护偏移量`](sparkExercise/src/main/scala/sparkAnalyze/sparkStreaming/readFile/ReadKafka1.scala)
 - [时间概念](sparkExercise/src/main/scala/sparkAnalyze/sparkStreaming/timeDuration/TimeDuration.scala)
    - 批处理间隔（Batch Duration）
    - 窗口时间宽度（Window Duration）
    - 滑动时间宽度（Slide Duration）
 - DStream基础转换操作
    
    |基础转换操作|说明|
    |:---|:---|
    |map(f)|遍历DStream中每一个元素，并应用f操作|
    |flatMap(f)|拆散；与map类似，但f返回的是一个集合|
    |filter(f)|返回DStream中满足f过滤条件的元素|
    |repartition(num)|通过改变分区数来改变运行的并行度|
    |union(other)|求两个DStream的并集|
    |[join(other, [num])](sparkExercise/src/main/scala/sparkAnalyze/sparkStreaming/dsTreamOpration/Join.scala)|将两个DStream(K,V)进行join，并返回一个新的DStream(K,(V,w))|
    |cogroup(other,[num])|将两个DStream(K,V)进行group，并返回一个DStream(K,Seq[V],Seq[W])|
    |count()|统计当前DStream中每个RDD中的元素数量（`count`在RDD中为`Action`算子）|
    |reduce(f)|将2个DStream中的每个RDD中的元素通过f进行聚合|
    |countByValue()|统计DStream（K）中每个元素出现的频次，并返回DStream(K, Long)|
    |[transform(f)](sparkExercise/src/main/scala/sparkAnalyze/sparkStreaming/dsTreamOpration/Transform.scala)|将当前DStream中某种类型的RDD通过f(在`Driver`中)转换成另一种类型，并封装到新的RDD中；一般用于对RDD直接进行操作|
    |[updateStateByKey(f)](sparkExercise/src/main/scala/sparkAnalyze/sparkStreaming/WordCount1.scala)|`有状态`转换。通过将Key原始状态与新状态通过f中定义的方式进行更新。可用于对某个统计变量进行`全局持续的累加`。|
    
 - 窗口转换操作
    
    |窗口转换操作|说明|
    |:---|:---|
    |window(wLength, slideInterval)|wLength:窗口时间宽度；slideInterval:滑动时间宽度；将原始DStream中的多个RDD按照窗口规则进行分组，并将每组RDD合并成一个RDD，存放在新的DStream中|
    |countByWindow(wLength,slideInterval)|返回窗口内RDD中元素的数量|
    |reduceByWindow(f,wLength,slideInterval)|在DStream中对窗口中的元素按照f进行`聚合`，并返回一个新的DStream|
    |[reduceByKeyAndWindow(f,wLength,slideInterval)](sparkExercise/src/main/scala/sparkAnalyze/sparkStreaming/timeDuration/TimeDuration.scala)|在（K，V）类型的DStream中，对窗口中的元素按照f进行`聚合`，并返回一个新的DStream|
    |[reduceByKeyAndWindow(f,wLength,slideInterval,[num])](sparkExercise/src/main/scala/sparkAnalyze/sparkStreaming/timeDuration/TimeDuration.scala)|`有状态`转换操作；reduceByKeyAndWindow的重载形式；|
    |countByValueAndWindow(wLength,slideInterval,[num])|统计窗口中DStream(K)内每个元素出现的频次，并返回DStream((K,Long))|
    
 - 输出操作
    
    |输出操作|说明|
    |:---|:---|
    |print()|在`Driver`进程中打印DStream中每个批次前10个元素。多用于调试|
    |saveAsTextFiles(prefix,[suffix])|将DStream中的数据保存到文件，可指定前后缀|
    |saveAsObjectFiles(prefix,[suffix])|将DStream中数据以对象形式保存到文件系统，数据序列化为SequenceFile|
    |saveHadoopFiles(prefix,[suffix])|将DStream中数据保存到Hadoop中|
    |[foreachRDD(f)](sparkExercise/src/main/scala/sparkAnalyze/sparkStreaming/dsTreamOpration/ForeachRDD.scala)|遍历DStream中每一个RDD，并对RDD执行f操作；f操作在`Driver`进程中调用；不要在foreachRDD中进行数据分析|
    
 - [checkpoint](sparkExercise/src/main/scala/sparkAnalyze/sparkStreaming/dsTreamOpration/CheckPoint.scala)
   > 通过checkpoint对数据进行备份，以便从故障中恢复
   - 分类
        - 元数据检查点
   			> 在Driver进程中恢复程序
   			- SparkConf相关信息
   			- DStream相关操作，如：转换和输出
   			- 队列等待处理的Job
   		- 数据检查点
   			- 实质上时RDD检查点
   			- `有状态转换`中会定期自动设置检查点，以切断上游的依赖
 - [累加器和广播变量](sparkExercise/src/main/scala/sparkAnalyze/sparkStreaming/WordCountAccumulator.scala)
 
 ***
## Spark小练习
- [wordCount](sparkExercise/src/main/scala/sparkAnalyze/sparkCore/practice/WordCount.scala) (`SparkCore`)读取指定文件，并统计文件单词个数
- [wordCount](sparkExercise/src/main/scala/sparkAnalyze/sparkStreaming/WordCount1.scala) `有状态转换`做`全局`词频统计
- [wordCount] 实现一个[`累加器`](sparkExercise/src/main/scala/sparkAnalyze/sparkStreaming/WordCountAccumulator.scala) ，并结合`无状态转换`操作，实现实时`全局`词频统计
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
    