package net.jcip.examples.ch11;

import java.util.concurrent.BlockingQueue;
/**
 * 真以为这完全是一个并发程序吗？
 * 不是的，对queue的访问控制还得是线性的，
 * 所以没有完全并发的程序。
 * @author Jelex.xu
 * @date 2017年8月27日
 */
public class WorkThread extends Thread {

	private final BlockingQueue<Runnable> queue;

	public WorkThread(BlockingQueue<Runnable> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		while(true) {
			Runnable task;
			try {
				task = queue.take();
				task.run();
			} catch (InterruptedException e) {
				//让线程退出
				break;
			}
		}
	}

	
}
