package cn.geeklemon;

import cn.geeklemon.consumer.ConsumerClient;
import cn.geeklemon.consumer.ConsumerLemon;
import cn.geeklemon.entity.ResultInfo;

public class TestDemo {
	public static void main(String[] args) {
		
//		for (int i = 0; i < 100; i++) {
//			ConsumerLemon client= new ConsumerLemon();
//			Object result = client.consume("geeklemon","/test2",new Object[] {String.valueOf(i)}).get();
//			System.out.println(((ResultInfo)result).getResult());
//		}
		ResultInfo eInfo = new ConsumerClient("geeklemon","127.0.0.1",8881).getResult("/test2", new Object[] {"hahaha"});
		System.out.println(eInfo.getResult());
		ResultInfo info = new ConsumerClient("geeklemon").getResult("/test2",new Object[] {"哈哈"});
		System.out.println(info.getResult());
	}
}
