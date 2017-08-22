package net.jcip.examples.ch08;

import java.util.concurrent.CountDownLatch;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

/**
 * 只要找一个方案即可！而不管好坏。
 * @author Jelex.xu
 * @date 2017年8月22日
 */
@ThreadSafe
public class ValueLatch<T> {

	@GuardedBy("this") private T value = null;
	
	private final CountDownLatch done = new CountDownLatch(1);
	
	public boolean isSet() {
		return (done.getCount() == 0);
	}
	
	public synchronized void setValue(T value) {
		if(!isSet()) {
			this.value = value;
			done.countDown();
		}
	}
	
	public T getValue() throws InterruptedException {
		done.await();
		synchronized(this) {
			return value;
		}
	}
}
