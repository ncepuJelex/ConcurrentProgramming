package net.jcip.examples.ch15;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.jcip.annotations.ThreadSafe;
/**
 * 使用lock实现的伪随机产生器，拿它和AtomicInteger做对比
 * 结论：contend 严重时，使用lock效果好，不严重时使用AtomicInteger好。
 * @author Jelex.xu
 * @date 2017年9月3日
 */
@ThreadSafe
public class ReentrantLockPseudoRandom extends PseudoRandom {
	//false argument means unfair lock.
	private final Lock lock = new ReentrantLock(false);
	
	private int seed;

	ReentrantLockPseudoRandom(int seed) {
		this.seed = seed;
	}
	
	public int nextInt(int n) {
		lock.lock();
		try {
			int s = seed;
			seed = calculateNext(s);
			int remainder = s % n;
			return remainder > 0 ? remainder : remainder + n;
		} finally {
			lock.unlock();
		}
	}
	
}
