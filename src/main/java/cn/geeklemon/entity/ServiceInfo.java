package cn.geeklemon.entity;

import java.util.HashSet;
import java.util.Set;

public class ServiceInfo {
	private String serviceName;
	private Set<String> addresses=new HashSet<>();
	
	public ServiceInfo(String serviceName) {
		this.serviceName=serviceName;
	}
	
	public int addAddress(String address) {
		addresses.add(address);
		return 1;
	}
	
	public int removeAddress(String address) {
		addresses.remove(address);
		return 1;
	}

	public String getServiceName() {
		return serviceName;
	}

	public Set<String> getAddresses() {
		return addresses;
	}

	public void setAddresses(Set<String> addresses) {
		this.addresses = addresses;
	}
	
	public int getAvailableCount() {
		return addresses.size();
	}
	
	public void addAllAddress(String[] address) {
		for (String string : address) {
			addresses.add(string);
		}
	}


}
