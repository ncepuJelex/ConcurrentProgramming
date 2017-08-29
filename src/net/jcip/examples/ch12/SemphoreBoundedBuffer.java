package net.jcip.examples.ch12;

import java.util.concurrent.Semaphore;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

/**
 * 生产环境中推荐使用ArrayBlockingQueue和LinkedBlockingQueue,
 * 这里只是演示如何用semaphore控制 插入 和 移除 元素。
 * @author Jelex.xu
 * @date 2017年8月28日
 */
@ThreadSafe
public class SemphoreBoundedBuffer<E> {

	private final Semaphore availableItem, availableSpaces;
	
	@GuardedBy("this")
	private final E[] items;
	
	@GuardedBy("this")
	private int putPosition = 0, takePosition = 0;

	public SemphoreBoundedBuffer(int capacity) {
		if(capacity < 0) {
			throw new IllegalArgumentException();
		}
		this.availableItem = new Semaphore(0);
		this.availableSpaces = new Semaphore(capacity);
		this.items = (E[]) new Object[capacity];
	}
	
	public boolean isEmpty() {
		return availableItem.availablePermits() == 0;
	}
	
	public boolean isFull() {
		return availableSpaces.availablePermits() == 0;
	}
	
	public void put(E x) throws InterruptedException {
		availableSpaces.acquire();
		doInsert(x);
		availableSpaces.release();
	}

	private synchronized void doInsert(E x) {
		
		int i = putPosition;
		items[i] = x;
		putPosition = (++i == items.length ? 0 : i);
	}
	
	public E take() throws InterruptedException {
		availableItem.acquire();
		E item = doExtract();
		availableItem.release();
		return item;
	}

	private synchronized E doExtract() {
		int i= takePosition;
		E x = items[i];
		items[i] = null;
		takePosition = (++i == items.length ? 0 : i);
		return x;
	}
}
