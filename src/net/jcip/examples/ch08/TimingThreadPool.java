package net.jcip.examples.ch08;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

/**
 * ThreadPoolExecutor 有几个勾子回调方法，可以覆盖使用，比如 
 * 开始执行前，执行结束后，池子关闭前。
 * @author Jelex.xu
 * @date 2017年8月21日
 */
public class TimingThreadPool extends ThreadPoolExecutor {

	private final ThreadLocal<Long> startTime = new ThreadLocal<Long>();
	private final Logger log = Logger.getLogger("TimingThreadPool");
	private final AtomicLong numTasks = new AtomicLong();
	private final AtomicLong totalTime = new AtomicLong();
	
	
	public TimingThreadPool() {
		super(1, 1, 0L, TimeUnit.SECONDS, null);
	}

	@Override
	protected void beforeExecute(Thread t, Runnable r) {
		super.beforeExecute(t, r);
		log.fine(String.format("Thread %s: start %s", t, r));
		startTime.set(System.nanoTime());
	}

	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		try {
			long endTime = System.nanoTime();
			long taskTime = endTime - startTime.get();
			numTasks.incrementAndGet();
			totalTime.addAndGet(taskTime);
			log.fine(String.format("Thread %s: end %s time=%dns", t, r, taskTime));
		} finally {
			super.afterExecute(r, t);
		}
	}

	@Override
	protected void terminated() {
		try {
			log.info(String.format("Terminated: avg time %dns", totalTime.get() / numTasks.get()));
		} finally {
			super.terminated();
		}
	}
	
	

	
}
