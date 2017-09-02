package net.jcip.examples.ch14;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

/**
 * 这个wait的前提有点意思！
 * @author Jelex.xu
 * @date 2017年9月2日
 */
@ThreadSafe
public class ThreadGate {

	@GuardedBy("this")
	private boolean isOpen;
	
	@GuardedBy("this")
	private int generation;
	
	public synchronized void close() {
		isOpen = false;
	}
	
	public synchronized void open() {
		++generation;
		isOpen = true;
		notifyAll();
	}
	
	public synchronized void await() throws InterruptedException {
		int arrivalGeneration = generation;
		while(!isOpen && arrivalGeneration == generation) {
			wait();
		}
	}
	
}
