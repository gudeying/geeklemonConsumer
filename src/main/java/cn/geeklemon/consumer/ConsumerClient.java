package cn.geeklemon.consumer;

import cn.geeklemon.entity.ConsumerClientHolder;
import cn.geeklemon.entity.ResultInfo;
import cn.geeklemon.entity.ServerParam;
import cn.geeklemon.exception.NoServiceException;

public class ConsumerClient {
	private String serviceName;
	private String servicePath;
	private String other;
	private String ip;
	private int port;
	private ServerParam param;
	private boolean useZk;

	/**
	 * 服务名称必须先在初始化类中添加过
	 * 
	 * @param serviceName
	 */
	public ConsumerClient(String serviceName) {
		this.serviceName = serviceName;
		ConsumerClientHolder.addClient(this);
	}

	public ConsumerClient(String serviceName, boolean useZK) {
		this.serviceName = serviceName;
		this.useZk = useZK;
		ConsumerClientHolder.addClientThatUseZK(this);
	}

	public ConsumerClient(String serviceName, String ip, int port) {
		this.ip = ip;
		this.serviceName = serviceName;
		this.port = port;
		ConsumerClientHolder.addClient(this);
	}

	public ResultInfo getResult(String path, Object[] params) {
		if (this.useZk) {
			return getResult(path, params, true);
		}
		return getResult(path, params, 0);
	}

	private ResultInfo getResult(String path, Object[] params, int falg) {
		ConsumerLemon consumerLemon = new ConsumerLemon();
		if (this.ip != null && port != 0) {
			return (ResultInfo) consumerLemon.consume(serviceName, path, params, ip, port, param).get();
		}

		while (!ConsumerClientHolder.serviceRegisted()) {
			synchronized (this) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		try {
			return (ResultInfo) consumerLemon.consume(serviceName, path, params).get();
		} catch (NoServiceException e) {
			e.printStackTrace();
		}
		return null;
	}

	private ResultInfo getResult(String path, Object[] params, boolean useZK) {
		ConsumerLemon consumerLemon = new ConsumerLemon();
		if (this.ip != null && port != 0) {
			return (ResultInfo) consumerLemon.consume(serviceName, path, params, ip, port, param).get();
		}

		while (!ConsumerClientHolder.serviceInited()) {
			synchronized (this) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		try {
			System.out.println("get result 。。。");
			return (ResultInfo) consumerLemon.consume(serviceName, path, params).get();
		} catch (NoServiceException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void notifyService() {
		synchronized (this) {
			notify();
		}
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServicePath() {
		return servicePath;
	}

	public void setServicePath(String servicePath) {
		this.servicePath = servicePath;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

}
