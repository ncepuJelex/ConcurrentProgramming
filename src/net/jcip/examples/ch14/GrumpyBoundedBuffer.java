package net.jcip.examples.ch14;

import net.jcip.annotations.ThreadSafe;

/**
 * 一个简陋的实现，和single thread中的处理一样，这哪里
 * 是讨论学习concurrent programming 的意义所在啊？
 * @author Jelex.xu
 * @date 2017年9月2日
 */
@ThreadSafe
public class GrumpyBoundedBuffer<V> extends BaseBoundedBuffer<V> {

	public GrumpyBoundedBuffer(int capacity) {
		super(capacity);
	}
	
	public synchronized void put(V v) throws BufferFullException {
		if(isFull()) {
			throw new BufferFullException();
		}
		doPut(v);
	}
	
	public synchronized V take() {
		if(isEmpty()) {
			throw new BufferEmptyException();
		}
		return doTake();
	}
}
class BufferFullException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}

class BufferEmptyException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}

/**
 * 客户端使用这个简陋的功能得这样处理了，不怎么好！
 * @author Jelex.xu
 * @date 2017年9月2日
 */
class ExampleUsage {
	
	private GrumpyBoundedBuffer<String> buffer;
	
	int SLEEP_GRANULARITY = 50;
	
	void useBuffer() throws InterruptedException {
		while(true) {
			try {
				String item = buffer.take();
				// use item
				break;
			} catch (BufferEmptyException e) {
				Thread.sleep(SLEEP_GRANULARITY);
			}
		}
	}
}
