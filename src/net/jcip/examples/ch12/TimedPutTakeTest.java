package net.jcip.examples.ch12;

import java.util.concurrent.CyclicBarrier;

public class TimedPutTakeTest extends PutTakeTest {

	private BarrierTimer timer = new BarrierTimer();
	
	public TimedPutTakeTest(int capacity, int nPairs, int nTrials) {
		super(capacity, nPairs, nTrials);
		barrier = new CyclicBarrier(nPairs * 2 + 1, timer);
	}

	@Override
	void test() {
		try {
			timer.clear();
			for(int i=0; i<nPairs; i++) {
				pool.execute(new PutTakeTest.Producer());
				pool.execute(new PutTakeTest.Consumer());
			}
			barrier.await();
			barrier.await();
			
			long nsPerItem = timer.getTime() / (nPairs * (long)nTrials);
			System.out.print("Throughput: " + nsPerItem+ "ns/item");
			
			assertEquals(putSum.get(), takeSum.get());
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args){
		
		int tpt = 1000000; //trials per thread
		for(int cap = 1; cap<= 1000; cap*=10) {
			System.out.println("Capacity: " + cap);
			for(int pairs = 1; pairs<=128; pairs*=2) {
				TimedPutTakeTest t = new TimedPutTakeTest(cap, pairs, tpt);
				System.out.print("Pairs: " + pairs + "\t");
				t.test();
				System.out.print("\t");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
				t.test();
				System.out.println();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}
		PutTakeTest.pool.shutdown();
	}
	
}
