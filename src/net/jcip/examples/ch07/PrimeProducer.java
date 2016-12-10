package net.jcip.examples.ch07;

import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;

public class PrimeProducer extends Thread {

	private final BlockingQueue<BigInteger> queue;

	public PrimeProducer(BlockingQueue<BigInteger> queue) {
		this.queue = queue;
	}

	public void run() {
		try {
			BigInteger p = BigInteger.ONE;
			while(!Thread.currentThread().isInterrupted()) {
				queue.put(p=p.nextProbablePrime());
			}
		} catch (InterruptedException e) {
			/*Allow thread to exit*/
		}
	}
	
	public void cancel() {
		interrupt();
	}
	
}
