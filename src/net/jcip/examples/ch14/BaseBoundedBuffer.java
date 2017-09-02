package net.jcip.examples.ch14;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

/**
 * 一个基类，基于数组的环形操作的buffer
 * @author Jelex.xu
 * @date 2017年9月2日
 */
@ThreadSafe
public class BaseBoundedBuffer<V> {

	@GuardedBy("this")
	private final V[] buf;
	
	@GuardedBy("this")
	private int tail;
	
	@GuardedBy("this")
	private int head;
	
	@GuardedBy("this")
	private int count;
	
	protected BaseBoundedBuffer(int capacity) {
		this.buf = (V[]) new Object[capacity];
	}
	
	protected synchronized final void doPut(V v) {
		buf[tail] = v;
		if(++tail == buf.length) {
			tail = 0;
		}
		count++;
	}
	
	protected synchronized final V doTake() {
		V v = buf[head];
		buf[head] = null;
		if(++head == buf.length) {
			head = 0;
		}
		count--;
		return v;
	}
	
	public synchronized final boolean isFull() {
		return count == buf.length;
	}
	
	public synchronized final boolean isEmpty() {
		return count == 0;
	}
}
