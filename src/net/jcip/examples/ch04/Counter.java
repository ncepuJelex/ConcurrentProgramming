package net.jcip.examples.ch04;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

/**
 * 对象的状态只由value这一个变量控制，而且上锁了，安全！
 * @author zhenhua
 * @date 2017年8月14日
 */
@ThreadSafe
public class Counter {

	@GuardedBy("this")
	private long value;
	
	public synchronized long getValue() {
		return value;
	}
	
	public synchronized long increment() {
		
		if(value == Long.MAX_VALUE) {
			throw new IllegalStateException();
		}
		return value ++;
	}
}
