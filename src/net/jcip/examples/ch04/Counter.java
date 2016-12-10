package net.jcip.examples.ch04;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

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
