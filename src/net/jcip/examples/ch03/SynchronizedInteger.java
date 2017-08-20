package net.jcip.examples.ch03;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

/**
 * 注意哦！仅仅在set方法上使用synchronized不行的，
 * 因为有可见性问题，所以你别以为get方法只是读取数据，
 * 不操作就不用加上syncrhonized关键字，小心stale data。
 * @author zhenhua
 * @date 2017年8月13日
 */
@ThreadSafe
public class SynchronizedInteger {

	@GuardedBy("this")
	private int value;

	public synchronized int getValue() {
		return value;
	}

	public synchronized void setValue(int value) {
		this.value = value;
	}
	
	
}
