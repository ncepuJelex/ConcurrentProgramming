package net.jcip.examples.ch14;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
/**
 * 现在put和take是各自独立的condition queue了，
 * 不用再notifyAll了，我吃饭、睡觉、旅行……
 * 我只需要这些事情时候叫醒我，你他Y的打游戏还
 * notify我就讨厌了，我不需要。那么你需要看看这个类。
 * @author Jelex.xu
 * @date 2017年9月2日
 */
@ThreadSafe
public class ConditionBoundedBuffer<T> {

	protected final Lock lock = new ReentrantLock();
	//等待该条件condition predicate:等待不满
	private final Condition notFull = lock.newCondition();
	private final Condition notEmpty = lock.newCondition();
	
	private static final int BUFFER_SIZE = 100;
	
	@GuardedBy("lock")
	private final T[] items = (T[]) new Object[BUFFER_SIZE];
	@GuardedBy("lock")
	private int tail, head, count;
	
	public void put(T x) throws InterruptedException {
		lock.lock();
		try {
			while (count == items.length) {
				notFull.await();
			}
			items[tail] = x;
			if (++tail == items.length) {
				tail = 0;
			}
			++count;
			notEmpty.signal();
		} finally {
			lock.unlock();
		}
	}
	
	public T take() throws InterruptedException {
		lock.lock();
		try {
			while (count == 0) {
				notEmpty.await();
			}
			T x = items[head];
			if (++head == items.length) {
				head = 0;
			}
			--count;
			notFull.signal();
			return x;
		} finally {
			lock.unlock();
		}
	}
	
}
