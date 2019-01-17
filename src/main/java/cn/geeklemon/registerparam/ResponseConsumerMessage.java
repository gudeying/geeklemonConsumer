package cn.geeklemon.registerparam;

public class ResponseConsumerMessage extends BaseMessage{
	private String status;
	private String serviceName;
	private String[] address;
	
	public ResponseConsumerMessage() {
		setMsgType(MsgType.RESPONSE_FOR_CONSUMER);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String[] getAddress() {
		return address;
	}

	public void setAddress(String[] address) {
		this.address = address;
	}

}
