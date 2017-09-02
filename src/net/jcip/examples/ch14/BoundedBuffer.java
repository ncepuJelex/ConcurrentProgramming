package net.jcip.examples.ch14;
/**
 * 这个程序本质上和上一个程序SleepyBoundedBuffer是一样的
 * 只是它利用Object中的wait和notifyAll方法，提高了response
 * 和 更好的利用了CPU resource.
 * @author Jelex.xu
 * @date 2017年9月2日
 */
public class BoundedBuffer<V> extends BaseBoundedBuffer<V> {

	// CONDITION PREDICATE: not-full(!isFull())
	// CONDITION PREDICATE: not-empty(!isEmpty())
	
	public BoundedBuffer() {
		this(100);
	}
	
	protected BoundedBuffer(int capacity) {
		super(capacity);
	}
	
	public synchronized void put(V v) throws InterruptedException {
		while(isFull()) {
			wait();
		}
		doPut(v);
		notifyAll();
	}
	
	public synchronized V take() throws InterruptedException {
		while(isFull()) {
			wait();
		}
		V v = doTake();
		notifyAll();
		return v;
	}

	/*
	 * 想要达到不想notifyAll的效果，conditional notification,
	 * 但是实现变复杂了啊
	 */
	public synchronized void alternatePut(V v) throws InterruptedException {
		while(isFull()) {
			wait();
		}
		boolean wasEmpty = isEmpty();
		doPut(v);
		if(wasEmpty) {
			notifyAll();
		}
	}
}
