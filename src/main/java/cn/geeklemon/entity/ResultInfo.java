package cn.geeklemon.entity;

import java.util.Date;

import cn.geeklemon.registerparam.BaseMessage;
import cn.geeklemon.registerparam.MsgType;

public class ResultInfo extends BaseMessage {
	private static ThreadLocal<ResultInfo> threadLocal = new ThreadLocal<>();

	public static ResultInfo getInstance() {
		ResultInfo instance = threadLocal.get();
		if (instance != null) {
			instance = new ResultInfo();
			threadLocal.set(instance);
		}
		return instance;

	}
	private int status;
	private Object result;
	private String other;
	private Date dateTime;

	public ResultInfo() {
		super();
		setMsgType(MsgType.REPLY);
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

}
