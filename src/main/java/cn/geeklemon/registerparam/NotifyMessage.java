package cn.geeklemon.registerparam;

public class NotifyMessage extends BaseMessage {
	private String serviceName;
	private String address;
	private String msg;

	public NotifyMessage() {
		setMsgType(MsgType.NOTIFY);
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
