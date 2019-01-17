package cn.geeklemon.entity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.aop.ThrowsAdvice;

import cn.geeklemon.consumer.zookeeper.CloudService;
import cn.geeklemon.consumer.zookeeper.ZookeeperParam;
import cn.geeklemon.consumer.zookeeper.ZookeeperService;
import cn.geeklemon.thread.RegisterThread;

/**
 * 	提供两种注册中心初始化服务地址的方式
 * @author kavingu
 *
 */
public class ConsumerHelper {
	public ConsumerHelper(ConsumerInitParam param) {
		RegisterThread mythread =null;
		ExecutorService service = Executors.newCachedThreadPool();
			 mythread = new RegisterThread(param.getIp(),param.getPort(), param.getServiceArr());
			 service.submit(mythread);
			 service.shutdown();

	}
	
	/**
	 * 	只启用 zookeeper 注册的地址信息
	 * @param consumerParam
	 * @param zkParam
	 */
	public ConsumerHelper(ConsumerInitParam consumerParam,ZookeeperParam zkParam) {
		ZookeeperService zService = new ZookeeperService(zkParam, consumerParam).connect();
		CloudService cloudService = new CloudService(zService,consumerParam.getServiceArr());
		cloudService.initServiceHolder();
	}
	
	/**
	 * 	获取两种注册中心的服务地址
	 * @param initParam
	 * @param zookeeperParam
	 * @param both
	 */
	public ConsumerHelper(ConsumerInitParam initParam,ZookeeperParam zookeeperParam,boolean both) {
		if (both) {
			ZookeeperService zService = new ZookeeperService(zookeeperParam,initParam).connect();
			CloudService cloudService = new CloudService(zService,initParam.getServiceArr());
			cloudService.initServiceHolder();
			
			RegisterThread mythread =null;
			ExecutorService service = Executors.newCachedThreadPool();
				 mythread = new RegisterThread(initParam.getIp(),initParam.getPort(), initParam.getServiceArr());
				 service.submit(mythread);
				 service.shutdown();
		}else {
			throw new RuntimeException();
		}
		
	}
}
