package cn.geeklemon.consumer.zookeeper;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

import cn.geeklemon.entity.ConsumerInitParam;
import cn.geeklemon.entity.ServiceHolder;
import cn.geeklemon.entity.ServiceInfo;

public class ZookeeperService implements Watcher {
	private ZooKeeper zooKeeper;
	private String coonString = "127.0.0.1:2181";
	private final CountDownLatch countDownLatch = new CountDownLatch(1);
	private CloudService cloudService;
	private Set<String> watchPath = new HashSet<>();
	private ConsumerInitParam consumerParam;

	@Override
	public void process(WatchedEvent event) {
		KeeperState keeperState = event.getState();
		EventType eventType = event.getType();
		if (event.getState() == KeeperState.SyncConnected) {
			System.out.println("zookeeper异步连接成功");
			countDownLatch.countDown();
		}
		// 循环注册监听器，以便监听服务删除信息
		try {
			for (String path : watchPath) {
				System.out.println(path);
				zooKeeper.getChildren(path, new EventWatcher(zooKeeper,consumerParam, watchPath));
			}

		} catch (KeeperException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	public ZookeeperService(ZookeeperParam param, ConsumerInitParam consumerParam) {
		this.consumerParam = consumerParam;
		this.coonString = param.getConnectString();
		Set<String> serviceSet = consumerParam.getServiceNames();
		for (String string : serviceSet) {
			String watch = "/geeklemoncloud/provider/" + string;
			watchPath.add(watch);
		}
	}

	public ZookeeperService connect() {
		try {
			zooKeeper = new ZooKeeper(coonString, 30000, this);
			countDownLatch.await();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return this;
	}

	public String createNode(String path, String data, CreateMode mode) throws Exception {
		return this.zooKeeper.create(path, data.getBytes(), Ids.OPEN_ACL_UNSAFE, mode);
	}

	public List<String> getChildren(String path) throws KeeperException, InterruptedException {
		List<String> children = zooKeeper.getChildren(path, false);
		return children;
	}

	public String getData(String path) throws KeeperException, InterruptedException {
		byte[] data = zooKeeper.getData(path, false, null);
		if (data == null) {
			return "";
		}
		return new String(data);
	}

	public Stat setData(String path, String data) throws KeeperException, InterruptedException {
		Stat stat = zooKeeper.setData(path, data.getBytes(), -1);
		return stat;
	}

	public void deleteNode(String path) throws InterruptedException, KeeperException {
		zooKeeper.delete(path, -1);
	}

	public void closeConnection() throws InterruptedException {
		if (zooKeeper != null) {
			zooKeeper.close();
		}
	}

	public boolean exits(String path) {
		Stat stat = null;
		try {
			stat = zooKeeper.exists(path, false);
		} catch (KeeperException | InterruptedException e) {
			e.printStackTrace();
		}
		if (stat == null) {
			return false;
		}
		return true;
	}

	/**
	 * 根据服务名获取可用的服务地址（ip:port）
	 * 
	 * @param serviceName
	 * @return
	 */
	public List<String> getAvailableService(String serviceName) {
		String zkPath = "/geeklemoncloud/provider/" + serviceName;
		if (!exits(zkPath)) {
			System.out.println("没有服务");
			return null;
		}
		/**
		 * {uid}
		 */
		List<String> services = new LinkedList<>();
		List<String> serviceAddrs = new LinkedList<>();
		try {
			services = getChildren(zkPath);
			for (String string : services) {
//				/geekleoncloud/provider/{serviceName}/{uid}
				String addr = getData(zkPath + "/" + string);
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
}
