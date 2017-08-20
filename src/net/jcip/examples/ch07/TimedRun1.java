package net.jcip.examples.ch07;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 自作主张！你又不知道r的interrupted policy是什么，你就规定在特定时间内
 * 没完成任务就interrupt当前线程，那r怎么办？
 * @author Jelex.xu
 * @date 2017年8月17日
 */
public class TimedRun1 {

	private static final ScheduledExecutorService cancelExec = Executors.newScheduledThreadPool(1);
	
	public static void timedRun(Runnable r, long timeout, TimeUnit unit) {
		final Thread taskThread = Thread.currentThread();
		cancelExec.schedule(new Runnable() {

			@Override
			public void run() {
				taskThread.interrupt();
			}
			
		}, timeout, unit);
		r.run();
	}
}
