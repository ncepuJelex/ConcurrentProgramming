package net.jcip.examples.ch07;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import net.jcip.examples.ch05.LaunderThrowable;

public class TimedRun {

	private static final ExecutorService taskExec = Executors.newCachedThreadPool();
	
	public static void timedRun(Runnable r,long timeout, TimeUnit unit) throws InterruptedException {
		Future<?> task = taskExec.submit(r);
		try {
			task.get(timeout, unit);
		} catch (ExecutionException e) {
			//exception thrown in task,rethrow
			throw LaunderThrowable.launderThrowable(e);
		} catch (TimeoutException e) {
			//task will be cancelled below
		} finally {
			//Harmless if task already completed
			task.cancel(true);//interrupt if running
		}
	}
}
