package net.jcip.examples.ch04;

import java.util.concurrent.atomic.AtomicInteger;

import net.jcip.annotations.NotThreadSafe;

/**
 * lower和upper变量是有关联的，所以这么一个composite关系就不能
 * 这样直接处理了！是线程不安全的，要上锁！
 * @author zhenhua
 * @date 2017年8月14日
 */
@NotThreadSafe
public class NumberRange {

//	INVARIANT:lower <= upper
	
	private final AtomicInteger lower = new AtomicInteger(0);
	private final AtomicInteger upper = new AtomicInteger(0);
	
	public void setLower(int i) {
		//Warning:unsafe check-then-act
		if(i > upper.get()) {
			throw new IllegalArgumentException("can't set lower to " + i + " > upper");
		}
		lower.set(i);
	}
	
	public void setUpper(int i) {
		//Warning:unsafe check-then-act
		if(i<lower.get()) {
			throw new IllegalArgumentException("cant set upper to " + i + " < lower");
		}
		upper.set(i);
	}
	
	public boolean isInRange(int i) {
		return i>= lower.get() && i<= upper.get();
	}
	
}
