package net.jcip.examples.ch13;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/**
 * 使用tryLock实现timed operation.
 * 同时可对取消操作响应
 * @author Jelex.xu
 * @date 2017年8月30日
 */
public class TimedLocking {

	private Lock lock = new ReentrantLock();

	public boolean trySendOnSharedLine(String message, long timeout, TimeUnit unit) throws InterruptedException {

		// 假设超时时间为10， 发送时间为4，那么试着获取锁的时间大约为10-4
		long nanosToLock = unit.toNanos(timeout) - estimatedNanosToSend(message);
		if (!lock.tryLock(nanosToLock, TimeUnit.NANOSECONDS)) {
			return false;
		}
		try {
			return sendOnSharedLine(message);
		} finally {
			lock.unlock();
		}
	}

	private boolean sendOnSharedLine(String message) {
		/*send message operation*/
		return true;
	}

	private long estimatedNanosToSend(String message) {
		return message.length();
	}
}
