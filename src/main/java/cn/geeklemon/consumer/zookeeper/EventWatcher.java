package cn.geeklemon.consumer.zookeeper;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;

import cn.geeklemon.entity.ConsumerInitParam;
import cn.geeklemon.entity.ServiceHolder;
import cn.geeklemon.entity.ServiceInfo;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventWatcher implements Watcher {
	private static final Logger LOGGER = LoggerFactory.getLogger(EventWatcher.class);
	private ZooKeeper zooKeeper;
	private ConsumerInitParam consumerParam;
	private Set<String> watchSet;

	public EventWatcher(ZooKeeper zooKeeper, ConsumerInitParam consumerParam, Set<String> watchSet) {
		this.zooKeeper = zooKeeper;
		this.consumerParam = consumerParam;
		this.watchSet = watchSet;
	}

	@Override
	public void process(WatchedEvent event) {
		if (event.getType().equals(EventType.NodeChildrenChanged)) {
			LOGGER.debug("服务节点发生变化");
			// 刷新服务列表
			freshServiceList(consumerParam.getServiceArr());
		}

		for (String watchPath : watchSet) {
			try {
				zooKeeper.getChildren(watchPath, this);
			} catch (KeeperException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void freshServiceList(String[] services) {
		/**
		 * 重新获取服务列表，由于不知道哪个节点没了，所有的服务都要重新更新，与注册中心不能共用，因为注册中心是可以监测到单个节点信息进行准确删除的
		 */
		ServiceHolder.clear();
		for (String serviceName : services) {
			Set<String> servicePath = new HashSet<>(getAvailableService(serviceName));
			ServiceInfo serviceInfo = new ServiceInfo(serviceName);
			serviceInfo.setAddresses(servicePath);
			ServiceHolder.registerService(serviceName, serviceInfo);
		}
	}

	private List<String> getAvailableService(String serviceName) {
		String zkPath = "/geeklemoncloud/provider/" + serviceName;
		try {
			if (null == zooKeeper.exists(zkPath, false)) {
				System.out.println("没有服务");
				return null;
			}
		} catch (KeeperException | InterruptedException e1) {
			e1.printStackTrace();
		}
		/**
		 * {uid}
		 */
		List<String> services = new LinkedList<>();
		List<String> serviceAddrs = new LinkedList<>();
		try {
			services = zooKeeper.getChildren(zkPath, false);
			for (String string : services) {
//				/geekleoncloud/provider/{serviceName}/{uid}
				byte[] bs = zooKeeper.getData(zkPath + "/" + string, false, null);
				String addr = null;
				if (bs != null) {
					addr = new String(bs);
				}

				if (null != addr) {
					serviceAddrs.add(addr);
				}
			}
		} catch (KeeperException | InterruptedException e) {
			e.printStackTrace();
			return null;
		}

		return serviceAddrs;
	}

}
