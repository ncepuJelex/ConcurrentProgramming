package net.jcip.examples.ch13;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 可被interrupted的功能操作，使用lock.lockInterruptibly()功能。
 * @author Jelex.xu
 * @date 2017年8月30日
 */
public class InterruptibleLocking {

	private Lock lock = new ReentrantLock();
	
	public boolean sendOnSharedLine(String message) throws InterruptedException {
		
		lock.lockInterruptibly();
		try {
			return cancellableSendOnSharedLine(message);
		} finally {
			lock.unlock();
		}
	}

	private boolean cancellableSendOnSharedLine(String message) {
		/*send message*/
		return true;
	}
}
