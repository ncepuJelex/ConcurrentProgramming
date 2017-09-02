package net.jcip.examples.ch14;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

import net.jcip.annotations.ThreadSafe;
/**
 * 模拟一个简单Latch，
 * acquire/acquireShared/release/releaseShared分别
 * 会调用tryAcquire/tryAcquiredShared/tryRealease/tryRealeaseShared方法
 * 所以程序中才实现try这种形式的方法。
 * @author Jelex.xu
 * @date 2017年9月2日
 */
@ThreadSafe
public class OneShotLatch {

	private final Sync sync = new Sync();
	
	public void signal() {
		sync.releaseShared(0);
	}
	
	public void await() throws InterruptedException {
		sync.acquireSharedInterruptibly(0);
	}
	
	private class Sync extends AbstractQueuedSynchronizer {
		private static final long serialVersionUID = 1L;

		@Override
		protected int tryAcquireShared(int arg) {
			return getState() == 1 ? 1 : -1;
		}

		@Override
		protected boolean tryReleaseShared(int arg) {
			setState(1);
			return true;
		}
		
		
	}
}
