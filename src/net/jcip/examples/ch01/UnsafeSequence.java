package net.jcip.examples.ch01;

import net.jcip.annotations.NotThreadSafe;

@NotThreadSafe
public class UnsafeSequence {

	private int value;
	
	/**
	 * Returns a unique value.
	 */
	public int getValue() {
		return value ++;
	}
}
