package net.jcip.examples.ch05;

import java.util.concurrent.BlockingQueue;

/**
 * 2种策略处理interruption,
 *  1.直接往外抛出;
 *  2.如果无法往外拋，那也要修复现场，如本程序中处理的那样，
 *  调用当前线程的interrupt()方法
 * @author zhenhua
 * @date 2017年8月15日
 */
public class TaskRunnable implements Runnable {

	BlockingQueue<Task> queue;
	
	@Override
	public void run() {
		try {
			processTask(queue.take());
		} catch(InterruptedException e) {
			//restore interrupted status
			Thread.currentThread().interrupt();
		}
	}
	
	private void processTask(Task take) {
		//Handle the task
	}

	interface Task {
		
	}
}
