package net.jcip.examples.ch07;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 把关闭后还没执行的任务记录下来，
 * 这个类可能作为一个工具类保存下来使用。
 * @author Jelex.xu
 * @date 2017年8月19日
 */
public class TrackingExecutor extends AbstractExecutorService {

	private final ExecutorService exec;
	
	private final Set<Runnable> tasksCancelledAtShutdown = 
			Collections.synchronizedSet(new HashSet<Runnable>());
	
	
	public TrackingExecutor(ExecutorService exec) {
		this.exec = exec;
	}

	@Override
	public void shutdown() {
		exec.shutdown();
	}

	@Override
	public List<Runnable> shutdownNow() {
		return exec.shutdownNow();
	}

	@Override
	public boolean isShutdown() {
		return exec.isShutdown();
	}

	@Override
	public boolean isTerminated() {
		return exec.isTerminated();
	}

	/*
	 * 超时之前terminated了的话，返回true,否则返回false.
	 * 这种情况表示：要么真的在很短时间内完成了执行任务，或者被打断了,interrupted.
	 * (non-Javadoc)
	 * @see java.util.concurrent.ExecutorService#awaitTermination(long, java.util.concurrent.TimeUnit)
	 */
	@Override
	public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
		return exec.awaitTermination(timeout, unit);
	}

	public List<Runnable> getCancelledTasks() {
		//还没执行结束，你就想得到取消的任务，疯了吧你！
		if(!exec.isTerminated()) {
			throw new IllegalStateException();
		}
		return new ArrayList<Runnable>(tasksCancelledAtShutdown);
	}
	
	@Override
	public void execute(Runnable command) {
		exec.execute(new Runnable() {

			@Override
			public void run() {
				try {
					command.run();
				} finally {
					//如果这个exec被关闭了，并且使用exec的线程被打断了，说明
					//是还未完成的任务，被中途打断了，下次需要接着执行完。
					if(isShutdown() && Thread.currentThread().isInterrupted()) {
						tasksCancelledAtShutdown.add(command);
					}
				}
			}
			
		});
	}

}
