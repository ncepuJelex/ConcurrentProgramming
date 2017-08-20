package net.jcip.examples.ch04;

import net.jcip.annotations.GuardedBy;
import net.jcip.examples.ch02.Widget;

/**
 * java monitor pattern,
 * private修饰myLock,还是final的，好！
 * @author zhenhua
 * @date 2017年8月14日
 */
public class PrivateLock {

	private final Object myLock = new Object();
	
	@GuardedBy("myLock")
	private Widget widget;
	
	void someMethod() {
		synchronized(myLock) {
			//Access or modify the state of widget
		}
	}
}
