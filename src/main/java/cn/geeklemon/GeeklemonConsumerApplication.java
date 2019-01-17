package cn.geeklemon;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sound.midi.Soundbank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import cn.geeklemon.consumer.ConsumerClient;
import cn.geeklemon.entity.ConsumerHelper;
import cn.geeklemon.entity.ConsumerInitParam;
import cn.geeklemon.entity.ResultInfo;

@SpringBootApplication
public class GeeklemonConsumerApplication {

	public static void main(String[] args) {
//		SpringApplication.run(GeeklemonConsumerApplication.class, args);
		ConsumerInitParam initParam = new ConsumerInitParam();
		initParam.addService("geeklemon");
		initParam.setIp("127.0.0.1");
		initParam.setPort(8899);
		new ConsumerHelper(initParam);//首先获取到可用服务地址到本地MAP中保存
		ConsumerClient client = new ConsumerClient("geeklemon");
		Object[] params = new Object[] {"哈哈哈哈"};
		ResultInfo resultInfo = client.getResult("/test2", params);
		System.out.println(resultInfo.getResult());
		
		
		
		Object[] userParam = {"id2"};
		ResultInfo resultInfo2 = client.getResult("getuser", userParam);
		System.out.println(resultInfo2.getResult()); 
//		ExecutorService service = Executors.newCachedThreadPool();
//		long start = System.currentTimeMillis();
//		for (int i = 0; i < 105; i++) {
//			final int is= i;
//			Runnable runnable = new Runnable() {
//				
//				@Override
//				public void run() {
//					
////					ConsumerInitParam initParam = new ConsumerInitParam();
////					initParam.addService("geeklemon");
////					initParam.setIp("127.0.0.1");
////					initParam.setPort(8899);
////					new ConsumerHelper(initParam);
////					ConsumerClient client = new ConsumerClient("geeklemon");
//					Object[] params = new Object[] {String.valueOf(is)};
//					ResultInfo resultInfo = client.getResult("getuser", params);
//					System.out.println(is+"---"+resultInfo.getResult());
//				}
//			};
//			
//			service.submit(runnable);
//		}
//		service.shutdown();
//		
//		for(;;) {
//			if (service.isTerminated()) {
//				System.out.println(System.currentTimeMillis()-start);
//				break;
//			}
//		}
	}
//	
	
}

