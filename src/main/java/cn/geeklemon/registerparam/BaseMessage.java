package cn.geeklemon.registerparam;

import java.io.Serializable;

public class BaseMessage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1852781081883589946L;
	private String uid;
	private MsgType msgType;

	public MsgType getMsgType() {
		return msgType;
	}

	public void setMsgType(MsgType msgType) {
		this.msgType = msgType;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
}
