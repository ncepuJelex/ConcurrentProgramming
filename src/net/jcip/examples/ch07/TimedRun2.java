package net.jcip.examples.ch07;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.jcip.examples.ch05.LaunderThrowable;

/**
 * 好的方法设计应该是这样的！把r的执行结果，或者interrupted后的现场信息
 * 保存起来，然后在指定时间后interrupt,
 * 然后看执行后的结果是啥
 * @author Jelex.xu
 * @date 2017年8月17日
 */
public class TimedRun2 {

	private static final ScheduledExecutorService cancelExec = Executors.newScheduledThreadPool(1);
	
	public static void timedRun(final Runnable r, long timeout, TimeUnit unit) throws InterruptedException {
		
		class RethrowableTask implements Runnable {

			private volatile Throwable t;
			
			@Override
			public void run() {
				try {
					r.run();
				} catch (Throwable t) {
					this.t = t;
				}
			}
			
			void rethrow() {
				if(t != null) {
					throw LaunderThrowable.launderThrowable(t);
				}
			}
		}
		
		RethrowableTask task = new RethrowableTask();
		final Thread taskThread = new Thread(task);
		taskThread.start();
		
		cancelExec.schedule(new Runnable() {

			@Override
			public void run() {
				taskThread.interrupt();
			}
			
		}, timeout, unit);
//		这样就共享错误信息了！
		taskThread.join(unit.toMillis(timeout));
		task.rethrow();
	}
}
