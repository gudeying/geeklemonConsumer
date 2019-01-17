package cn.geeklemon.registerparam;

public class AddressRequestMessage extends BaseMessage {
	private String serviceName;

	public AddressRequestMessage() {
		super();
		setMsgType(MsgType.FOR_ADDRESS);
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
}
