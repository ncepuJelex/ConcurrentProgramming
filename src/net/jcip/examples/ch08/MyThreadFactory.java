package net.jcip.examples.ch08;

import java.util.concurrent.ThreadFactory;

/**
 * 这样可以在thread dumps和错误日志分析中更好区别。
 * 好吧！或许只是为了使用下ThreadFactory。
 * @author Jelex.xu
 * @date 2017年8月21日
 */
public class MyThreadFactory implements ThreadFactory {

	private final String poolName;
	
	public MyThreadFactory(String poolName) {
		this.poolName = poolName;
	}

	@Override
	public Thread newThread(Runnable r) {
		
		return new MyAppThread(r, poolName);
	}

}
