package cn.geeklemon.client;

import cn.geeklemon.registerparam.RequestServiceMessage;

public class ServiceClient {
	private String serviceName;
	private String method;

	public ServiceClient(String serviceName) {
		this.serviceName = serviceName;
	}
	
	public String request(String method,String[] param) {
		RequestServiceMessage message = new RequestServiceMessage();
		message.setServiceName(serviceName);
		message.setServicePath(method);
		message.setParams(formatParam(param));
		return null;
	}
	
	private String formatParam(String [] params) {
		StringBuilder builder = new StringBuilder();
		for (String string : params) {
			builder.append(string).append("-");
		}
		return builder.toString();
	}

}
