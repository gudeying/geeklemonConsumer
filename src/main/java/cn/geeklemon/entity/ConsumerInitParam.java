package cn.geeklemon.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * 	使用服务必须要先到服务注册中心注册，信息包括：注册中心地址；需要提供的服务
 * @author kavingu
 *
 */
public class ConsumerInitParam {
	private Set<String> serviceNames;
	private String uid;
	private String ip;
	private int port;

	public void addService(String serviceName) {
		if (serviceNames != null) {
			serviceNames.add(serviceName);
			return;
		}
		serviceNames = new HashSet<String>();
		serviceNames.add(serviceName);

	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public Set<String> getServiceNames() {
		return serviceNames;
	}
	
	public String[] getServiceArr() {
		String[] strings = new String[serviceNames.size()];
		strings = serviceNames.toArray(strings);
		return strings;
	}
	public void setServiceNames(Set<String> serviceNames) {
		this.serviceNames = serviceNames;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}
