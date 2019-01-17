package cn.geeklemon.client;

import java.util.concurrent.CountDownLatch;

import cn.geeklemon.registerparam.AddressRequestMessage;
import cn.geeklemon.registerparam.RequestMessage;
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

public class LemonClientThread implements Runnable{
	public static final CountDownLatch countDownLatch = new CountDownLatch(1);
	private Channel channel;
	@Override
	public void run() {
		EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).handler(new ChannelInitializer<Channel>() {

				@Override
				protected void initChannel(Channel ch) throws Exception {
					ChannelPipeline p = ch.pipeline();
					p.addLast(new ObjectEncoder());
					p.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
					p.addLast(new ClientStringTest());
				}
			});

			channel = bootstrap.connect("localhost", 8899).sync().channel();
			
			AddressRequestMessage message  = new AddressRequestMessage();
			message.setServiceName("geeklemon");
			channel.writeAndFlush(message);
			countDownLatch.await();
			channel.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			eventLoopGroup.shutdownGracefully();
		}		
	}
	
	public Channel getChannel() {
		return channel;
	}
	
	public String serve(String serviceName,RequestMessage message) {
		return "";
	}
}
