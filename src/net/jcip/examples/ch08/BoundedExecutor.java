package net.jcip.examples.ch08;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;

import net.jcip.annotations.ThreadSafe;

/**
 * 使用Semaphore的功能来处理任务量过大，
 * 使任务队列阻塞的问题，限制任务来的速率。
 * @author Jelex.xu
 * @date 2017年8月21日
 */
@ThreadSafe
public class BoundedExecutor {

	private final Executor exec;
	private final Semaphore semaphore;
	
	public BoundedExecutor(Executor exec, int bound) {
		this.exec = exec;
		this.semaphore = new Semaphore(bound);
	}
	
	public void submitTask(final Runnable runnable) throws InterruptedException {
		semaphore.acquire();
		try {
			exec.execute(new Runnable() {

				@Override
				public void run() {
					try {
						runnable.run();
					} finally {
						semaphore.release();
					}
				}
			});
		} catch (RejectedExecutionException e) {
			semaphore.release();
		}
	}
	
}
