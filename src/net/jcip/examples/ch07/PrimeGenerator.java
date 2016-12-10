package net.jcip.examples.ch07;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

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
