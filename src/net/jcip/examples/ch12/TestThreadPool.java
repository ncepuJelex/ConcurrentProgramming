package net.jcip.examples.ch12;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.TestCase;
/**
 * 测试资源泄露 resource leak
 * @author Jelex.xu
 * @date 2017年8月29日
 */
public class TestThreadPool extends TestCase {

	private final TestThreadFactory threadFactory = new TestThreadFactory();
	
	public void testPoolExpansion() throws InterruptedException {
		int MAX_SIZE = 10;
		ExecutorService exec = Executors.newFixedThreadPool(MAX_SIZE);
		for(int i=0; i<10 * MAX_SIZE; i++) {
			exec.execute(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(Long.MAX_VALUE);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
			});
		}
		
		for(int i=0; i<20 && threadFactory.numCreated.get() < MAX_SIZE; i++) {
			Thread.sleep(100);
		}
		
		assertEquals(threadFactory.numCreated.get(), MAX_SIZE);
		exec.shutdownNow();
	}
	
	/*
	 * 加了个计数功能
	 */
	class TestThreadFactory implements ThreadFactory {

		public final AtomicInteger numCreated = new AtomicInteger();
		private final ThreadFactory factory = Executors.defaultThreadFactory();
		
		@Override
		public Thread newThread(Runnable r) {
			numCreated.incrementAndGet();
			return factory.newThread(r);
		}
		
	}
}
