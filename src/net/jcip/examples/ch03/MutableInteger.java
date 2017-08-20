package net.jcip.examples.ch03;

import net.jcip.annotations.NotThreadSafe;

/**
 * 这个没什么讲的吧！就是非线程安全！
 * @author zhenhua
 * @date 2017年8月13日
 */
@NotThreadSafe
public class MutableInteger {

	private int value;

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	
}
