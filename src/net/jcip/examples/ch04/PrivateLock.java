package net.jcip.examples.ch04;

import net.jcip.annotations.GuardedBy;
import net.jcip.examples.ch02.Widget;

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
