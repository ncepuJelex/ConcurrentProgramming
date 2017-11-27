package net.jcip.examples.ch07;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * one shot execution service，
 * 如果一个方法需要执行一批任务，并且直到所有任务都完成了再返回，那么就可以通过使用一个
 * 私有的、生命周期和和方法绑定的Executor 来简化服务的生命周期管理(invokeAll和invokeAny
 * 方法在这种情况下很有用)。
 * @author Jelex.xu
 * @date 2017年8月19日
 */
public class CheckForMail {

	public boolean checkMail(Set<String> hosts, long timeout, TimeUnit unit) throws InterruptedException {
		
		ExecutorService exec = Executors.newCachedThreadPool();
		final AtomicBoolean hasNewMail = new AtomicBoolean(false);
		
		try {
			for (final String host : hosts) {
				exec.execute(new Runnable() {

					@Override
					public void run() {
						if (checkMail(host)) {
							hasNewMail.set(true);
						}
					}
				});
			} 
		} finally {
			exec.shutdown();
			exec.awaitTermination(timeout, unit);
		}
		return hasNewMail.get();
	}
	
	private boolean checkMail(String host) {
		return false;
	}
}
