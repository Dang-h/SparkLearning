# Spark Shuffle解析以及内存管理

## Spark Shuffle解析
### ShuffleMapStage和ResultStage
![shuffleMapStage_ResultStage](assert/shuffleMapStage_ResultStage.png)
- 在划分Stage时，最后一个Stage称为`finalStage`，本质上一个`ResultStage`对象，在ResultStage之前的Stage称为`ShuffleMapStage`。
- `shuffleMapStage`的结束伴随着**Shuffle文件的写磁盘** 。
- `resultMapStage`基本上对应代码中的`ation`算子，即，将一个函数应用在RDD的各个Partition的数据集上，**意味着一个job的运行结束**。
### Shuffle中任务个数
 Spark Shuffle分为`map`（ShuffleRead）阶段和`reduce`（ShuffleWrite）阶段，每个阶段都会由若干个`task`来执行，如何确定`mapTask`和`reduceTask`的数量呢？
 假设Spark任务从HDFS中读取数据，那么，**初始RDD分区个数**由该文件的`split`个数决定，一个`split`对应生成一个`partition`，假设初始`partition`个数为**N**。
 初始RDD经过一些列计算后（没有执行`repartition`和`coalesce`进行重分区），分区数不变，当执行shuffle操作时，**map端的task个数和partition个数一致**，即，`mapTask`个数为**N**。
 reducce端的stage默认取spark.default.parallelism配置的分区数（默认为**父RDD**中最大分区数），即，`reduceTask`个数为**N**。
 ### reduce端数据的读取
 `mapTask`位于`ShuffleMapStage`，`reduceTask`位于`ResultStage`，mapTask 会先执行，那么后执行的 reduceTask 如何知道从哪里去拉取 mapTask 落盘后的数据呢？
 reduce端拉取数据过程如下：
1. `mapTask`执行完毕后会将计算状态以及磁盘小文件位置等信息封装到`mapStatus`对象中，然后由本进程中的`mapOutPutTrackerWorker`对象将`mapstatus`对象发送给`Driver
`进程的`mapOutPutTrackerMaster`对象；
2. 在`reduceTask`开始执行之前会额按本进程中的`mapOutPutTrackerWorker`向`Driver`进程中的`mapOutPutTrackerMaster`发送请求，请求磁盘小文件位置；
3. 当所有的`mapTask`执行完毕后，`Driver`进程中`mapOutPutTrackerMaster`会告诉`mapOutPutTrackerWorker`磁盘小文件的位置信息；
4. 完成之前的操作之后，由BlockTransformService去Executor0所在节点拉取数据，默认启动5歌子线程。每次拉去的数据量不超过48MB（reduceTask每次最多拉取48MB
数据，将拉来的数据存储到Executor内的20%内存（通过[`参数`](../../../../../../README.md#优化资源)来设置）中）
### HashShuffle和SortShuffle
#### HashShuffle
1. 未经优化的HashShuffleManager
`shuffleWrite`阶段，作用：在一个`stage结束计算之后`，下一个stage可以执行`shuffle类算子`（如：reduceByKey），而将每个task处理的数据`按key
`进行“划分”;所谓的`划分`就是对`相同的Key`执行`Hash算法`，从而将`相同Key`写入同一个磁盘文件中，而每一个磁盘文件都属于`下游Stage`的一个`task`。再将数据写入磁盘之前，会**先将数据写入内存缓冲中，当内存缓冲填满之后，才会溢写到磁盘文件中**。
**下一个stage的task有多少个，当前stage的每个task就要创建多少分磁盘文件**。比如：下一个stage共100个task，那么当前的stage的每个task都要创建100分磁盘文件，即，一共要从创建（100*100=）10000个磁盘文件。如果当前stage有50个task，总共10个Executor，每个Executor执行5个Task，那么每个Executor上总共要创建（50*10=）500个磁盘文件
#### SortShuffle
## Spark 内存管理