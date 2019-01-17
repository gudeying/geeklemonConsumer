package cn.geeklemon.registerparam;

import java.util.LinkedList;
import java.util.List;

public class RequestMessage extends BaseMessage{
	private String serviceName;
	private List<String> params = new LinkedList<>();
	private String servicePath;
	
	public RequestMessage() {
		super();
		setMsgType(MsgType.SERVICE);
	}
	public RequestMessage addParam(String param) {
		params.add(param);
		return this;
	}
	
	public Object[] getParamArr() {
		return params.toArray();
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public List<String> getParams() {
		return params;
	}
	public void setParams(List<String> params) {
		this.params = params;
	}
	public String getServicePath() {
		return servicePath;
	}
	public void setServicePath(String servicePath) {
		this.servicePath = servicePath;
	}
	
	
}