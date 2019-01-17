package cn.geeklemon.entity;

import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import cn.geeklemon.consumer.ConsumerClient;

public class ConsumerClientHolder {
	/**
	 * 	请求服务所保存的client，为了请求返回之后能够唤醒等待
	 */
	private static volatile boolean serviceRegisted = false;
	private static volatile boolean serviceInited = false;
	private static ConcurrentHashMap<String, ConsumerClient> map = new ConcurrentHashMap<>();
	private static ConcurrentHashMap<String, ConsumerClient> zkUsemap = new ConcurrentHashMap<>();
	private static AtomicInteger integer = new AtomicInteger(0);
	public ConsumerClient getClient(String id) {
		return map.get(id);
	}
	public static void addClient(ConsumerClient client) {
		String id = String.valueOf(integer.incrementAndGet());
		map.put(id, client);
	}
	
	public static void addClientThatUseZK(ConsumerClient client) {
		String id = String.valueOf(integer.incrementAndGet());
		zkUsemap.put(id, client);
	}
	
	public static void notifyClient() {
		ConsumerClientHolder.serviceRegisted=true;
		for (Entry<String, ConsumerClient> clientMap : map.entrySet()) {
			clientMap.getValue().notifyService();
		}
		map.clear();
	}
	
	
	public static void notifyClientThatUseZK() {
		serviceInited = true;
		for (Entry<String,ConsumerClient> clientMap:zkUsemap.entrySet()) {
			clientMap.getValue().notifyService();
		}
	}
	
	public static boolean serviceRegisted() {
		return ConsumerClientHolder.serviceRegisted;
	}
	
	public static boolean serviceInited() {
		return serviceInited;
	}
}
