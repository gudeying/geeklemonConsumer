package cn.geeklemon;

import cn.geeklemon.consumer.ConsumerClient;
import cn.geeklemon.consumer.zookeeper.ZookeeperParam;
import cn.geeklemon.entity.ConsumerHelper;
import cn.geeklemon.entity.ConsumerInitParam;
import cn.geeklemon.entity.ResultInfo;

public class ClientTest {
	public static void main(String[] args) {
		/**
		 * 使用zookeeper注册的服务
		 */
		ConsumerInitParam initParam = new ConsumerInitParam();
		initParam.addService("geeklemon");
		initParam.setIp("127.0.0.1");
		initParam.setPort(8899);
		ZookeeperParam zookeeperParam = new ZookeeperParam();
		zookeeperParam.setConnectString("127.0.0.1:2181");
		zookeeperParam.setTimeOut(3000);
//		zookeeperParam.setServiceRootPath("/geeklemon");
		new ConsumerHelper(initParam,zookeeperParam);
		

		ConsumerClient client = new ConsumerClient("geeklemon", true);
		ResultInfo  resultInfo = client.getResult("/test2",new Object[] {"zookeeper shuju"});
		System.out.println(resultInfo.getResult());
	}
}
