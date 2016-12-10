package net.jcip.examples.ch05;

import java.util.concurrent.BlockingQueue;

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
