package net.jcip.examples.ch14;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.jcip.annotations.GuardedBy;

/**
 * 使用lock来模拟Semaphore，其实Semaphore内部
 * 不是使用lock来实现的啦！
 * 但是他们share同一个基类，QueuedSynchronizer(AQS)
 * 学习完这个程序，就去吃饭，黄焖鸡吧，哎，
 * 继上次查到海底捞的厨房不卫生后，后来又查到黄焖鸡了，
 * 希望别搞事情啊！
 * @author Jelex.xu
 * @date 2017年9月2日
 */
public class SemaphoreOnLock {

	private final Lock lock = new ReentrantLock();
	private Condition permitsAvailable = lock.newCondition();
	
	@GuardedBy("lock")
	private int permits;

	public SemaphoreOnLock(int initialPermits) {
		lock.lock();
		try {
			permits = initialPermits;
		} finally {
			lock.unlock();
		}
	}
	
	public void acquire() throws InterruptedException {
		lock.lock();
		try {
			//没有permits了，等待其它线程释放再取吧！
			while (permits <= 0) {
				permitsAvailable.await();
			}
			--permits;
		} finally {
			lock.unlock();
		}
	}
	
	public void release() {
		lock.lock();
		try {
			++permits;
			permitsAvailable.signal();
		} finally {
			lock.unlock();
		}
	}
	
}
