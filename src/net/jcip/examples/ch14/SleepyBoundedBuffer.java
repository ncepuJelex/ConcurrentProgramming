package net.jcip.examples.ch14;

import net.jcip.annotations.ThreadSafe;

/**
 * 无法操作，那就去沉睡会，和上一个例子GrumpyBoundedBuffer 相比，
 * 就是替客户端擦了屁股，把while(true)那些东西做了，客户直接调用
 * 操作方法即可。
 * @author Jelex.xu
 * @date 2017年9月2日
 */
@ThreadSafe
public class SleepyBoundedBuffer<V> extends BaseBoundedBuffer<V> {

	int SLEEP_GRANULARITY = 60;
	
	/*
	 * Implicit super constructor BaseBoundedBuffer<V>() is undefined.
	 *  Must explicitly invoke another constructor
	 *  一个空的无参构造方法竟然还不行！
	 */
	public SleepyBoundedBuffer() {
		this(100);
	}

	public SleepyBoundedBuffer(int capacity) {
		super(capacity);
	}

	public void put(V v) throws InterruptedException {
		while(true) {
			synchronized(this) {
				if(!isFull()) {
					doPut(v);
					return;
				}
			}
			Thread.sleep(SLEEP_GRANULARITY);
		}
	}
	
	public V take() throws InterruptedException {
		while(true) {
			synchronized(this) {
				if(!isEmpty()) {
					return doTake();
				}
			}
			Thread.sleep(SLEEP_GRANULARITY);
		}
	}
	
}
