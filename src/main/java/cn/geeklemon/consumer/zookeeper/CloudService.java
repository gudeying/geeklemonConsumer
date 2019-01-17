package cn.geeklemon.consumer.zookeeper;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.zookeeper.KeeperException;

import cn.geeklemon.entity.ConsumerClientHolder;
import cn.geeklemon.entity.ServiceHolder;
import cn.geeklemon.entity.ServiceInfo;
import cn.geeklemon.exception.NoServiceException;

public class CloudService {
	private ZookeeperService zookeeperService;
	/**
	 * 	消费者需要的服务名称列表
	 */
	private String[] services;
	
	public CloudService(ZookeeperService zookeeperService,String[]services) {
		this.zookeeperService =zookeeperService;
		this.services = services;
	}
	
	/**
	 * 	根据服务名获取可用的服务地址（ip:port）
	 * @param serviceName
	 * @return
	 */
	public List<String> getAvailableService(String serviceName) {
		String zkPath = "/geeklemoncloud/provider/" + serviceName;
		if (!zookeeperService.exits(zkPath)) {
			System.out.println("没有服务");
			return null;
		}
		/**
		 * 	{uid}
		 */
		List<String> services = new LinkedList<>();
		List<String> serviceAddrs = new LinkedList<>();
		try {
			services = zookeeperService.getChildren(zkPath);
			for (String string : services) {
//				/geekleoncloud/provider/{serviceName}/{uid}
				String addr = zookeeperService.getData(zkPath+"/"+string);
				if (!addr.equals("")) {
					serviceAddrs.add(addr);
				}
			}
		} catch (KeeperException | InterruptedException e) {
			e.printStackTrace();
			return null;
		}

		return serviceAddrs;
	}

	
	public void initServiceHolder() {
		for (String serviceName : services) {
			Set<String> servicePath = new HashSet<>(getAvailableService(serviceName));
			ServiceInfo serviceInfo = new ServiceInfo(serviceName);
			serviceInfo.setAddresses(servicePath);
			ServiceHolder.registerService(serviceName, serviceInfo);
		}
		try {
			System.out.println(ServiceHolder.getOneService("geeklemon", false));
		} catch (NoServiceException e) {
			e.printStackTrace();
		}
		if (!ConsumerClientHolder.serviceRegisted()) {
			ConsumerClientHolder.notifyClientThatUseZK();
		}
	}
	
	public void refreshServiceHolder() {
		initServiceHolder();
	}
	
	
}
