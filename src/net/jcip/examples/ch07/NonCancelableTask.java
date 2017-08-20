package net.jcip.examples.ch07;

import java.util.concurrent.BlockingQueue;

/**
 * Noncancelable task that restores interruption before exit
 * 好任性啊！一直while，直到取到东西，管它有没有被interrupted，
 * 如果interrupted了，最后在finally 修复现场再离开不就好了！
 * 
 */
public class NonCancelableTask {

	public Task getNextTask(BlockingQueue<Task> queue) {
		boolean interrupted = false;
		try {
			
			while(true) {
				try {
					return queue.take();
				} catch (InterruptedException e) {
					interrupted = true;
					//fail through and retry
				}
			}
		} finally {
			if(interrupted) {
				Thread.currentThread().interrupt();
			}
		}
	}
	interface Task {
		
	}
}
