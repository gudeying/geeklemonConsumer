package cn.geeklemon.registerparam;

import java.util.Date;

public class AddressReplyMessage extends BaseMessage {
	private int status;

	private String address;
	private String result;

	private Date dateTime;

	public AddressReplyMessage() {
		super();
		setMsgType(MsgType.FOR_ADDRESS);
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
}
