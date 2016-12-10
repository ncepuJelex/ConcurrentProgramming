package net.jcip.examples.ch07;

import java.util.concurrent.BlockingQueue;

/**
 * Noncancelable task that restores interruption before exit
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
