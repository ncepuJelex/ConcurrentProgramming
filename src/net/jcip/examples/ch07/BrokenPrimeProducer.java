package net.jcip.examples.ch07;

import java.math.BigInteger;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 阻塞住了，完了，谁也别玩了！
 * 如果producer跑在consumer前面，queue会被灌满然后put会阻塞住，
 * 然后consumer拼命地去设置cancel标识，然并卵，因为producer卡住了，根本不会去
 * 检查cancel标识（都怪你consumer消费不及时，不从queue中取数，让我的put操作阻塞住了）
 * @author Jelex.xu
 * @date 2017年8月17日
 */
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
