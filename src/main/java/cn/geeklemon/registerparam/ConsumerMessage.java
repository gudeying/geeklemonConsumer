package cn.geeklemon.registerparam;

public class ConsumerMessage extends BaseMessage {
	private String[] serviceName;
	private String string;

	public ConsumerMessage() {
		super();
		setMsgType(MsgType.CONSUMER);
	}

	

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}



	public String[] getServiceName() {
		return serviceName;
	}



	public void setServiceName(String[] serviceName) {
		this.serviceName = serviceName;
	}

}
