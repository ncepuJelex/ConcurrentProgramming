package net.jcip.examples.ch15;

import java.util.concurrent.atomic.AtomicInteger;

import net.jcip.annotations.ThreadSafe;
/**
 * AtomicInteger来实现伪随机数。
 * @author Jelex.xu
 * @date 2017年9月3日
 */
@ThreadSafe
public class AtmoicPsedoRandom extends PseudoRandom {

	private final AtomicInteger seed;

	AtmoicPsedoRandom(int seed) {
		this.seed = new AtomicInteger(seed);
	}
	
	public int nextInt(int n) {
		while(true) {
			int s = seed.get();
			int nextSeed = calculateNext(s);
			if(seed.compareAndSet(s, nextSeed)) {
				int remainder = s % n;
				return remainder > 0 ? remainder : remainder + n;
			}
		}
	}
	
}
