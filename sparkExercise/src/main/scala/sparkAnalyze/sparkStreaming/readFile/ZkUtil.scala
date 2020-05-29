package sparkAnalyze.sparkStreaming.readFile

import org.I0Itec.zkclient.ZkClient
import org.I0Itec.zkclient.exception.ZkMarshallingError
import org.I0Itec.zkclient.serialize.ZkSerializer

/*
用于初始化ZKClient
通过ZKClient手动与Zookeeper建立通信，并读取和保存Kafka的消费偏移量
 */
object ZkUtil {

	/**
	 * 用于返回ZKClient实例
	 *
	 * @param zkServers zookeeper集群地址
	 * @param sessionTimeout 会话超时时间
	 * @param connectionTimeout 连接超时时间
	 * @return
	 */
	def initZKClient(zkServers: String, sessionTimeout: Int, connectionTimeout: Int): ZkClient = {

		new ZkClient(zkServers, sessionTimeout, connectionTimeout, new ZkSerializer {
			override def serialize(data: Any): Array[Byte] = {
				try (data.toString.getBytes("UTF-8"))
				catch {
					case _: ZkMarshallingError => null
				}
			}
			override def deserialize(bytes: Array[Byte]): AnyRef = {
				try (new String(bytes, "UTF-8"))
				catch {
					case _: ZkMarshallingError => null
				}
			}
		})
	}
}
