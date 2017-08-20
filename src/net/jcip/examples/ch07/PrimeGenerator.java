package net.jcip.examples.ch07;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

/**
 * 通过设置flag位，这里是cancelled字段，
 * 来达到取消线程执行的目的，
 * 这里在while循环里是去寻找下一个奇数，可能还好点，
 * 如果执行的任务阻塞住了，不会去检查cancelled字段的值，就
 * 不会有机会退出while循环，那就苦逼了。所以这个方法不好。
 * @author zhenhua
 * @date 2017年8月17日
 */
@ThreadSafe
public class PrimeGenerator implements Runnable {

	private static ExecutorService exec = Executors.newCachedThreadPool();
	
	@GuardedBy("this")
	private final List<BigInteger> primes = new ArrayList<>();
	
	private volatile boolean cancelled;
	
	@Override
	public void run() {
		BigInteger p = BigInteger.ONE;
		while(!cancelled) {
			p = p.nextProbablePrime();
			synchronized(this) {
				primes.add(p);
			}
		}
	}
	
	public void cancel() {
		cancelled = true;
	}
	
	public synchronized List<BigInteger> get() {
		return new ArrayList<BigInteger>(primes);
	}
	
	static List<BigInteger> aSecondOfPrimes() throws InterruptedException {
		
		PrimeGenerator generator = new PrimeGenerator();
		exec.submit(generator);
		/*
		 * lets the prime generator run for one second before cancelling it.
		 */
		try {
			SECONDS.sleep(1);
		} finally {
			generator.cancel();
		}
		return generator.get();
	}

}
