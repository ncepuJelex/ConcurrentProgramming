package net.jcip.examples.ch07;

import java.math.BigInteger;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BrokenPrimeProducer extends Thread {

	private final BlockingQueue<BigInteger> queue;
	
	private volatile boolean cancelled = false;
	
	BrokenPrimeProducer(BlockingQueue<BigInteger> queue) {
		this.queue = queue;
	}
	
	public void run() {
		BigInteger p = BigInteger.ONE;
		while(!cancelled) {
			try {
				queue.put(p=p.nextProbablePrime());
			} catch (InterruptedException e) {
			}
		}
	}
	
	public void cancel() {
		cancelled = true;
	}
	
	
	/**
	 * 
	 */
	void consumePrimes() throws InterruptedException {
		BlockingQueue<BigInteger> primes = new ArrayBlockingQueue<BigInteger>(10);
		BrokenPrimeProducer producer = new BrokenPrimeProducer(primes);
		producer.start();
		try {
			while(needMorePrimes()) {
				consume(primes.take());
			}
		} finally {
			producer.cancel();
		}
	}

	private void consume(BigInteger take) {
		
	}

	private boolean needMorePrimes() {
		return false;
	}
}
