package cn.geeklemon.client;

import java.util.concurrent.CountDownLatch;

import cn.geeklemon.entity.ResultInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServiceRequestHandler  extends SimpleChannelInboundHandler<ResultInfo>{
	private  CountDownLatch COUNT_DOWN_LATCH;
	public ServiceRequestHandler() {
	}

	public ServiceRequestHandler(CountDownLatch countDownLatch){
		this.COUNT_DOWN_LATCH = countDownLatch;
	}
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ResultInfo msg) throws Exception {
		
		if (msg.getStatus()!=0) {
			System.out.println(msg.getResult());
		}else {
			System.out.println("请求过程出错了");
			System.out.println(msg.getUid());
		}
		ctx.channel().close();
		COUNT_DOWN_LATCH.countDown();
	}
}
