package net.jcip.examples.ch12;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.TestCase;

/**
 * 测试SemphoreBoundedBuffer。
 * @author Jelex.xu
 * @date 2017年8月28日
 */
public class PutTakeTest extends TestCase {

	protected static final ExecutorService pool = Executors.newCachedThreadPool();
	protected CyclicBarrier barrier;
	protected final SemphoreBoundedBuffer<Integer> bb;
	protected final int nTrials, nPairs;
	protected final AtomicInteger putSum = new AtomicInteger(0);
	protected final AtomicInteger takeSum = new AtomicInteger(0);
	
	public PutTakeTest(int capacity, int nPairs, int nTrials) {
		this.bb = new SemphoreBoundedBuffer<Integer>(capacity);
		this.nTrials = nTrials;
		this.nPairs = nPairs;
		this.barrier = new CyclicBarrier(nPairs * 2 + 1);
	}

	public static void main(String[] args) {
		new PutTakeTest(10, 10, 100000).test();
		pool.shutdown();
	}
	
	void test() {
		try {
			for(int i=0; i<nPairs; i++) {
				pool.execute(new Producer());
				pool.execute(new Consumer());
			}
			barrier.await(); // wait for all thread to be ready
			barrier.await(); //wait for all thread to finish
			assertEquals(putSum.get(), takeSum.get());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	static int xorShift(int y) {
        y ^= (y << 6);
        y ^= (y >>> 21);
        y ^= (y << 7);
        return y;
    }
	
	class Producer implements Runnable {

		@Override
		public void run() {
			try {
				int seed = (this.hashCode()) ^ (int)System.nanoTime();
				int sum = 0;
				barrier.await();
				for(int i=nTrials; i>0; i--) {
					bb.put(seed);
					sum += seed;
					seed = xorShift(seed);
				}
				putSum.getAndAdd(sum);
				barrier.await();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	class Consumer implements Runnable {

		@Override
		public void run() {
			try {
				barrier.await();
				int sum = 0;
				for(int i=nTrials; i>0; i--) {
					sum += bb.take();
				}
				takeSum.getAndAdd(sum);
				barrier.await();
			} catch (Exception e) {
			}
			
		}
		
	}
}
