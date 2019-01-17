package cn.geeklemon.client;

import cn.geeklemon.entity.ResultInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<ResultInfo>{

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ResultInfo msg) throws Exception {
		
		if (msg.getStatus()!=0) {
			System.out.println(msg.getResult());
		}else {
			System.out.println("请求过程出错了");
		}
		Client.countDownLatch.countDown();
	}

}
