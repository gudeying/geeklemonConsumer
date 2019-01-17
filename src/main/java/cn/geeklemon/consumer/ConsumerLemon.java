package cn.geeklemon.consumer;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import cn.geeklemon.entity.ResultInfo;
import cn.geeklemon.entity.ServerParam;
import cn.geeklemon.entity.ServiceHolder;
import cn.geeklemon.exception.NoServiceException;
import cn.geeklemon.registerparam.RequestMessage;
import cn.geeklemon.thread.TaskFuture;

public class ConsumerLemon {
	private String serviceName;
	private boolean DONE = false;
	private ResultInfo resultInfo;

	/**
	 * 	从注册中心获取服务地址进行服务请求
	 * @param serviceName
	 * @param path
	 * @param params
	 * @return
	 * @throws NoServiceException
	 */
	public ConsumerLemon consume(String serviceName, String path, Object[] params) throws NoServiceException {
		
		RequestMessage message = new RequestMessage();
		message.setServiceName(serviceName);
		message.setServicePath(path);
		for (Object object : params) {
			message.addParam((String) object);
		}
		ServerParam param = new ServerParam();
		param.setBalance(false);
		TaskFuture.requestResult(this, (RequestMessage) message, param);
		return this;
	}

	
	/**
	 * 	直接使用知道服务地址的服务
	 * @param serviceName
	 * @param path
	 * @param params
	 * @param ip
	 * @param port
	 * @param param
	 * @return
	 */
	public ConsumerLemon consume(String serviceName, String path, Object[] params, String ip, int port,
			ServerParam param) {
		RequestMessage message = new RequestMessage();
		message.setServiceName(serviceName);
		message.setServicePath(path);
		for (Object object : params) {
			message.addParam((String) object);
		}
		TaskFuture.requestResult(this, (RequestMessage) message, param, ip, port);
		return this;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public void setDone(ResultInfo resultInfo) {
		synchronized (this) {
			this.resultInfo = resultInfo;
			DONE = true;
			notifyAll();
		}
	}

	public Object get() {
		synchronized (this) {
			try {
				while (!DONE) {
					wait();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return resultInfo;
		}
		
	}
}
