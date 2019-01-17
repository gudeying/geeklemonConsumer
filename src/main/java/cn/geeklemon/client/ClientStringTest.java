package cn.geeklemon.client;

import cn.geeklemon.registerparam.AddressReplyMessage;
import cn.geeklemon.registerparam.RequestMessage;
import cn.geeklemon.registerparam.RequestServiceMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientStringTest extends SimpleChannelInboundHandler<AddressReplyMessage>{
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, AddressReplyMessage msg) throws Exception {
		if (msg.getStatus()==0) {
			System.out.println(msg.getUid());
		}else {
			System.out.println("可用地址："+msg.getAddress());
			String address = msg.getAddress();
			String addr = address.split(":")[0];
			int port = Integer.parseInt(address.split(":")[1]);
			RequestMessage requestMsg = new RequestMessage();
			requestMsg.setServicePath("/test2");
			requestMsg.addParam("balabala");
			ServiceRequestServer.getResult(addr, port, "/test2", requestMsg);
		}
	}

}
