package cn.geeklemon.thread;

import java.util.concurrent.CompletionService;
import java.util.concurrent.ConcurrentHashMap;
import cn.geeklemon.entity.ResultInfo;

public class ConsumerThreadMap {
	private static ConcurrentHashMap<String, CompletionService<ResultInfo>> map = new ConcurrentHashMap<>();
	
	public static void add(String id,CompletionService<ResultInfo>service) {
		map.put(id, service);
	}
	
	public static void remove(String id) {
		map.remove(id);
	}
}
