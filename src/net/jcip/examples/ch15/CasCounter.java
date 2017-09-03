package net.jcip.examples.ch15;

import net.jcip.annotations.ThreadSafe;
/**
 * 使用CAS的小demo,想到AtomicInteger，它的get()方法
 * 原理是不是也是这样封装……
 * @author Jelex.xu
 * @date 2017年9月3日
 */
@ThreadSafe
public class CasCounter {

	private SimulatedCAS value;
	
	public int getValue() {
		return value.get();
	}
	
	public int increment() {
		int v;
		do {
			v = value.get();
		}while(v != value.compareAndSwap(v, v+1));
		return v+1;
	}
}
