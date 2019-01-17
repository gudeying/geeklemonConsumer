package cn.geeklemon.thread;

import java.util.concurrent.CountDownLatch;

import cn.geeklemon.entity.ResultInfo;
import cn.geeklemon.registerparam.RequestMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class RequestThread implements Runnable {
	private static final CountDownLatch countDownLatch = new CountDownLatch(1);
	private ResultInfo resultInfo;
	private RequestMessage message;
	private String ip;
	private int port;

	public RequestThread(RequestMessage msg, String ip, int port) {
		this.message = msg;
		this.ip = ip;
		this.port = port;
	}

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
					p.addLast(new SimpleChannelInboundHandler<ResultInfo>() {

						@Override
						protected void channelRead0(ChannelHandlerContext ctx, ResultInfo msg) throws Exception {
							if (msg.getStatus() != 0) {
							} else {
								System.out.println("请求过程出错了");
								System.out.println(msg.getUid());
							}
							System.out.println("result："+msg);
							resultInfo = msg;
							String id = msg.getUid();
							TaskFuture.map.get(id).setDone(resultInfo);
							TaskFuture.map.remove(id);//释放强引用，防止内存泄漏
							countDownLatch.countDown();
							eventLoopGroup.shutdownGracefully();
						}
					});
				}
			});

			Channel channel = bootstrap.connect(ip, port).sync().channel();

			channel.writeAndFlush(message);
			System.out.println("已经发送请求");
			countDownLatch.await();
//			channel.close();
		} catch (Exception e) {
			e.printStackTrace();
			eventLoopGroup.shutdownGracefully();
			String id = message.getUid();
			TaskFuture.map.get(id).setDone(resultInfo);
		} finally {
//			eventLoopGroup.shutdownGracefully();
		}
	}

}
