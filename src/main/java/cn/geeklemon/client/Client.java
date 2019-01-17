package cn.geeklemon.client;

import java.util.concurrent.CountDownLatch;

import cn.geeklemon.registerparam.RequestMessage;
import cn.geeklemon.registerparam.RequestServiceMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class Client {
	public static final CountDownLatch countDownLatch = new CountDownLatch(1);

	public static void main(String[] args) {
		EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).handler(new ChannelInitializer<Channel>() {

				@Override
				protected void initChannel(Channel ch) throws Exception {
					ChannelPipeline p = ch.pipeline();
					p.addLast(new ObjectEncoder());
					p.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
					p.addLast(new ClientHandler());
//					p.addLast(new ClientStringTest());
				}
			});

			Channel channel = bootstrap.connect("localhost", 8891).sync().channel();
			RequestServiceMessage message = new RequestServiceMessage();
			message.setServiceName("testService");
			message.setServicePath("/test2");
			message.setParams("lalala");
			
			RequestMessage requestMsg = new RequestMessage();
			requestMsg.setServicePath("/test2");
			requestMsg.addParam("balabala");
			
			
			channel.writeAndFlush(requestMsg);
			countDownLatch.await();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			eventLoopGroup.shutdownGracefully();
		}
	}

}
