package cn.geeklemon.thread;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.geeklemon.consumer.ConsumerLemon;
import cn.geeklemon.entity.ServerParam;
import cn.geeklemon.entity.ServiceHolder;
import cn.geeklemon.exception.NoServiceException;
import cn.geeklemon.registerparam.RequestMessage;

public class TaskFuture {
	private static final Logger LOGGER = LoggerFactory.getLogger(TaskFuture.class);
	
	private static ExecutorService threadPool = Executors.newFixedThreadPool(3);
	private static AtomicInteger integer = new AtomicInteger(0);
	public static ConcurrentHashMap<String, ConsumerLemon> map = new ConcurrentHashMap<>();

	public static void requestResult(ConsumerLemon client, RequestMessage message, ServerParam param) throws NoServiceException {
		String serviceName = message.getServiceName();
		String address = ServiceHolder.getOneService(serviceName, param.isBalance());
		LOGGER.info("use {} to serve",address);
		String[] strings = address.split(":");
		String ip = strings[0];
		int port = Integer.parseInt(strings[1]);
		String id = String.valueOf(integer.incrementAndGet());
		message.setUid(id);

		map.put(id, client);
		RequestThread task = new RequestThread(message, ip, port);
		threadPool.submit(task);
		/* 系统运行中会不断产生一个线程来进行服务，线程池不能关闭，实验中要关闭程序才会正确结束*/
//		threadPool.shutdown();  
	}
	
	public static void requestResult(ConsumerLemon client,RequestMessage message,
			ServerParam param,String ip,int port) {
		String id = String.valueOf(integer.incrementAndGet());
		message.setUid(id);

		map.put(id, client);
		RequestThread task = new RequestThread(message, ip, port);
		threadPool.submit(task);
//		threadPool.shutdown();
	}
	
}
