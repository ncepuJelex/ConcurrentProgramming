package net.jcip.examples.ch15;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
/**
 * CAS:compare and set机制，刻atguigu有位老师模拟过这个算法，
 * 回头看看。
 * @author Jelex.xu
 * @date 2017年9月3日
 */
@ThreadSafe
public class SimulatedCAS {

	@GuardedBy("this")
	private int value;
	
	public synchronized int get() {
		return value;
	}
	
	public synchronized int compareAndSwap(int expectedValue, int newValue) {
		int oldValue = value;
		if(oldValue == expectedValue) {
			value = newValue;
		}
		return oldValue;
	}
	
	public synchronized boolean compareAndSet(int expectedValue, int newValue) {
		return expectedValue == compareAndSwap(expectedValue, newValue);
	}
}
