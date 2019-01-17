package cn.geeklemon.entity;

import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.geeklemon.exception.NoServiceException;
import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue.WaitStrategy;

public class ServiceHolder {
	private volatile static boolean service;
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceHolder.class);
	static ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	static Lock readLock = readWriteLock.readLock();
	static Lock writeLoke = readWriteLock.writeLock();

	/**
	 * 存储serviceName对应的服务信息
	 */
	private static final ConcurrentHashMap<String, ServiceInfo> serviceHolder = new ConcurrentHashMap<>();

	public static ServiceInfo getServiceInfo(String serviceName) {
		return serviceHolder.get(serviceName);
	}

	public static void registerService(String serviceName, ServiceInfo serviceInfo) {
		writeLoke.lock();
		try {
			serviceHolder.put(serviceName, serviceInfo);
		} finally {
			writeLoke.unlock();
		}
	}

	/**
	 * 移除不可用服务的地址
	 * 
	 * @param serviceName
	 * @param address
	 */
	public static void removeAddress(String serviceName, String address) {
		writeLoke.lock();
		try {
			serviceHolder.get(serviceName).removeAddress(address);
			if (serviceHolder.get(serviceName).getAvailableCount() == 0) {
				serviceHolder.remove(serviceName);
			} 
		} finally {
			writeLoke.unlock();
		}
	}

	/**
	 * 移除一个服务
	 * 
	 * @param serviceName
	 */
	public static void removeService(String serviceName) {
		writeLoke.lock();
		try {
			serviceHolder.remove(serviceName);
		} finally {
			writeLoke.unlock();
		}
	}

	/**
	 * 根据服务名获取所有可用的服务
	 * 
	 * @param serviceName
	 * @return
	 */
	public static Set<String> getServiceAddrs(String serviceName) {
		readLock.lock();
		try {
			return serviceHolder.get(serviceName).getAddresses();
		} finally {
			readLock.unlock();
		}
	}

	/**
	 * 获取一个服务
	 * 
	 * @param serviceName
	 * @param balance
	 * @return ip:port
	 * @throws NoServiceException
	 */
	public static String getOneService(String serviceName, boolean balance) throws NoServiceException {

		readLock.lock();
		try {
			String[] addrArr;
			ServiceInfo serviceInfo = serviceHolder.get(serviceName);
			if (serviceInfo == null) {
				throw new NoServiceException("没有获取到-" + serviceName + "-相关的服务");
			}
			addrArr = new String[serviceInfo.getAvailableCount()];
			addrArr = serviceInfo.getAddresses().toArray(addrArr);
			int length = addrArr.length;
			LOGGER.info("可用服务数：{}",length);
			if (addrArr.length == 0) {
				throw new NoServiceException("没有可用服务地址");
			}
			if (!balance && length > 1) {

				int random = new Random().nextInt(length);
				LOGGER.info("serve with :" + addrArr[random]);
				return addrArr[random];
			}
			return addrArr[0];
		} finally {
			readLock.unlock();
		}
	}

	public void setIsService(boolean service) {
		this.service = service;
	}

	public static boolean isServiceStart() {
		return ServiceHolder.service;
	}
	
	public static int serviceAvailableCount(String serviceName) {
		return serviceHolder.get(serviceName).getAvailableCount();
	}
	
	public static void clear() {
		writeLoke.lock();
		try {
			serviceHolder.clear();
		} finally {
			writeLoke.unlock();
		}
	}
}
