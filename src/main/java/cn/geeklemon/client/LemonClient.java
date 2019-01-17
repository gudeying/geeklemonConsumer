package cn.geeklemon.client;

import java.util.concurrent.CountDownLatch;

import cn.geeklemon.Handler.BaseHandler;
import cn.geeklemon.entity.ServiceHolder;
import cn.geeklemon.registerparam.AddressRequestMessage;
import cn.geeklemon.registerparam.ConsumerMessage;
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

public class LemonClient {
	private static String serviceName="geeklemon";
	private static String ip = "127.0.0.1";
	private static int port = 8899;
	private static boolean alive = true;
	private static Channel channel;
	static EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
	public static void main(String[] args) {
	
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).handler(new ChannelInitializer<Channel>() {

				@Override
				protected void initChannel(Channel ch) throws Exception {
					ChannelPipeline p = ch.pipeline();
					p.addLast(new ObjectEncoder());
					p.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
					p.addLast(new BaseHandler());
				}
			});

			channel = bootstrap.connect("localhost", 9988).sync().channel();
			ConsumerMessage message = new ConsumerMessage();
					
			message.setServiceName(new String[] {serviceName});
			channel.writeAndFlush(message);
		
		} catch (Exception e) {
			e.printStackTrace();
			eventLoopGroup.shutdownGracefully();
		}
	}
	
	public static void shutDown() {
		eventLoopGroup.shutdownGracefully();
	}
}
