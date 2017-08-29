package net.jcip.examples.ch12;

import java.util.concurrent.atomic.AtomicInteger;
/**
 * 一个中等级别的生产随机的方法，不能用RNG,random函数，
 * 因为那其实都是可预测的，编译器会自作聪明地提前给定值。
 * @author Jelex.xu
 * @date 2017年8月28日
 */
public class XorShift {

	static final AtomicInteger seq = new AtomicInteger(8862213);
	
	int x = -1831433054;
	
	public XorShift(int seed) {
		x = seed;
	}
	
	public XorShift() {
		this((int) System.nanoTime() + seq.getAndAdd(129));
	}
	
	public int next() {
		x ^= x<<6;
		x ^= x>>>21;
		x ^= x<<7;
		return x;
	}
}
