package cn.geeklemon.Handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.geeklemon.consumer.ConsumerClient;
import cn.geeklemon.entity.ConsumerClientHolder;
import cn.geeklemon.entity.ResultInfo;
import cn.geeklemon.entity.ServiceHolder;
import cn.geeklemon.entity.ServiceInfo;
import cn.geeklemon.registerparam.BaseMessage;
import cn.geeklemon.registerparam.NotifyMessage;
import cn.geeklemon.registerparam.ResponseConsumerMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class BaseHandler extends SimpleChannelInboundHandler<BaseMessage>{
	public static final Logger LOGGER = LoggerFactory.getLogger(BaseHandler.class);
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, BaseMessage msg) throws Exception {
		switch (msg.getMsgType()) {
		case RESPONSE_FOR_CONSUMER: 
			LOGGER.info("收到服务地址信息");
			/*收到服务信息*/
			ResponseConsumerMessage message = (ResponseConsumerMessage)msg;
			String serviceName = message.getServiceName();
			String[] addresses = message.getAddress();
			if (addresses == null) {
				break;
			} 
			LOGGER.info("服务方个数：{}",String.valueOf(addresses.length));
			ServiceInfo serviceInfo = new ServiceInfo(serviceName);
			serviceInfo.addAllAddress(addresses);
			ServiceHolder.registerService(message.getServiceName(), serviceInfo);
			ConsumerClientHolder.notifyClient();
			break;
		case NOTIFY:
			LOGGER.info("服务变化");
			/*收到服务变化信息*/
			NotifyMessage notifyMessage =  (NotifyMessage)msg;
			String changeServiceName = notifyMessage.getServiceName();
			String changeAddr = notifyMessage.getAddress();
			ServiceHolder.removeAddress(changeServiceName, changeAddr);
			LOGGER.info("{}服务地址{}已不可用",changeServiceName,changeAddr);
			break;
		default:
			break;
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
		ctx.channel().close();
		
	}
}
