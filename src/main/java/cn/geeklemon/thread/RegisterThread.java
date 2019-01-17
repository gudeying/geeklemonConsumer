package cn.geeklemon.thread;

import cn.geeklemon.Handler.BaseHandler;
import cn.geeklemon.registerparam.ConsumerMessage;
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

public class RegisterThread implements Runnable {
	private Channel channel;
	private EventLoopGroup eventLoopGroup;
	private String centerIp = "127.0.0.1";
	private int centerPort = 8899;
	private String[] serviceName;
	private boolean alive;

	public RegisterThread() {

	}

	public RegisterThread(String centerIp, int centerPort, String[] serviceName) {
		this.centerIp = centerIp;
		this.serviceName = serviceName;
		this.centerIp = centerIp;
	}

	@Override
	public void run() {
		try {
			eventLoopGroup = new NioEventLoopGroup();
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
			ConsumerMessage message = new ConsumerMessage();
			message.setServiceName(serviceName);
			channel = bootstrap.connect(centerIp, centerPort).sync().channel();
			channel.writeAndFlush(message);

		} catch (Exception e) {
			e.printStackTrace();
			eventLoopGroup.shutdownGracefully();
		}
	}

	public void shutDown() {
		eventLoopGroup.shutdownGracefully();
	}
}
