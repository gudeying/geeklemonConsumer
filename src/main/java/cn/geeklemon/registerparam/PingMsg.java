package cn.geeklemon.registerparam;

public class PingMsg extends BaseMessage{
	public PingMsg() {
		super();
		setMsgType(MsgType.PING);
	}
}
