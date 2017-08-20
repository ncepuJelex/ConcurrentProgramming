package net.jcip.examples.ch05;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * 这是一个经典案例，测试多个线程同时执行任务，到最后一个执行结束所
 * 需要的时间
 * @author zhenhua
 * @date 2017年8月15日
 */
public class TestHarness {

	public static long timeTasks(int nThreads, Runnable task) throws InterruptedException {
		
		final CountDownLatch startGate = new CountDownLatch(1);
		final CountDownLatch endGate = new CountDownLatch(nThreads);
		
		for(int i=0; i<nThreads; i++) {
			new Thread() {
				@Override
				public void run() {
					try {
						startGate.await();
						try {
							task.run();
						} finally {
							endGate.countDown();
						}
					} catch (InterruptedException e) {
						
					}
				}
				
			}.start();
		}
		
		long start = System.nanoTime();
		startGate.countDown();
		
		endGate.await();
		
		long end = System.nanoTime();
		return end - start;
	}
	
	public static void main(String[] args) {
		
		String [] names = {"CR7","RONALDO","Messi","Jelex","fk"};
		
		Runnable task = new Runnable() {
			@Override
			public void run() {
				System.out.println(names[new Random().nextInt(5)]);
			}
			
		};
		
		try {
			System.out.print(timeTasks(3,task));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
