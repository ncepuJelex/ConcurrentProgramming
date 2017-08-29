package net.jcip.examples.ch12;

import junit.framework.TestCase;

public class TestBoundedBuffer extends TestCase {

	private static final long LOOKUP_DETECT_TIMEOUT = 1000;
	private static final int CAPACITY = 10000;
	private static final int THRESHOLD = 10000;
	
	void testIsEmptyWhenConstructed() {
		SemphoreBoundedBuffer<Integer> sbb = new SemphoreBoundedBuffer<Integer>(10);
		assertTrue(sbb.isEmpty());
		assertFalse(sbb.isFull());
	}
	
	void testFullAfterPuts() throws InterruptedException {
		SemphoreBoundedBuffer<Integer> bb = new SemphoreBoundedBuffer<Integer>(10);
		for(int i=0; i<10; i++) {
			bb.put(i);
		}
		assertTrue(bb.isFull());
		assertFalse(bb.isEmpty());
	}
	
	void testTakeWhenEmpty() {
		final SemphoreBoundedBuffer<Integer> bb  = new SemphoreBoundedBuffer<Integer>(10);

		Thread taker = new Thread(){
			@Override
			public void run() {
				try {
					Integer unused = bb.take();
					fail(); //if we get here, it'a an error.
				} catch (InterruptedException success) {
				}
			}
		};
		
		try {
			taker.start();
			Thread.sleep(LOOKUP_DETECT_TIMEOUT);
			taker.interrupt();
			taker.join(LOOKUP_DETECT_TIMEOUT);
			assertFalse(taker.isAlive());
		} catch (InterruptedException e) {
			fail();
		}
	}
	
	class Big {
		double [] data = new double[100000];
	}
	
	void testLeak() throws InterruptedException {
		SemphoreBoundedBuffer<Big> bb = new SemphoreBoundedBuffer<Big>(CAPACITY);
		int heapSize = snapshotHeap();
		for(int i=0; i<CAPACITY; i++) {
			bb.put(new Big());
		}
		for(int i=0; i<CAPACITY; i++) {
			bb.take();
		}
		int heapSize2 = snapshotHeap();
		assertTrue(Math.abs(heapSize - heapSize2) < THRESHOLD);
	}

	private int snapshotHeap() {
		return 0;
	}
}
