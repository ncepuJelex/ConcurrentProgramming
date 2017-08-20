package net.jcip.examples.ch05;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.Semaphore;

/**
 * 有大小限制的set,但是它自己不知道，
 * 这是通过Semaphore来控制的，add成功的时候
 * 你占了semphore的一个“名额”，当add的数量达到semphore控制
 * 的大小时，就无法add了，但是你可以通过remove来释放一个“名额”
 * 的目的，但是能添加的总量是固定的。
 * @author zhenhua
 * @date 2017年8月16日
 */
public class BoundedHashSet<T> {

	private final Set<T> set;
	private final Semaphore sem;
	
	public BoundedHashSet(Set<T> set, Semaphore sem) {
		this.set = Collections.synchronizedSet(set);
		this.sem = sem;
	}
	
	public boolean add(T o) throws InterruptedException {
		sem.acquire();
		boolean wasAdded = false;
		try {
			wasAdded = set.add(o);
			return wasAdded;
		} finally {
			if(!wasAdded) {
				sem.release();
			}
		}
	}
	
	public boolean remove(Object o) {
		boolean removed = set.remove(o);
		if(removed) {
			sem.release();
		}
		return removed;
	}
}
