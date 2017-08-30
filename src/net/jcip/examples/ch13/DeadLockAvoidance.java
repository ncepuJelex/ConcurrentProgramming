package net.jcip.examples.ch13;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
/**
 * 转账业务，这里使用tryLock方式实现，很屌啊！还带随机时间等待的。
 * @author Jelex.xu
 * @date 2017年8月30日
 */
public class DeadLockAvoidance {

	private static Random rnd = new Random();

	public boolean transferMoney(Account fromAcct, Account toAcct, DollarAmount amount, long timeout, TimeUnit unit)
			throws InsufficientFundException, InterruptedException {

		long fixedDelay = getFixedDelayComponentNanos(timeout, unit);
		long randMod = getRandomDelayModulusNanos(timeout, unit);
		long stopTime = System.nanoTime() + unit.toNanos(timeout);
		
		while(true) {
			if(fromAcct.lock.tryLock()) {
				try {
					if(toAcct.lock.tryLock()) {
						try {
							if(fromAcct.getBalance().compareTo(amount) < 0) {
								throw new InsufficientFundException();
							} else {
								fromAcct.debit(amount);
								toAcct.credit(amount);
								return true;
							}
						} finally {
							toAcct.lock.unlock();
						}
					}
				} finally {
					fromAcct.lock.unlock();
				}
			}
			//在超时范围内完成转账
			if(System.nanoTime() < stopTime) {
				return false;
			}
			//等待随机时间再次尝试
			TimeUnit.NANOSECONDS.sleep(fixedDelay + rnd.nextLong() % randMod);
		}
	}

	private long getRandomDelayModulusNanos(long timeout, TimeUnit unit) {
		return DELAY_RANDOM;
	}

	private static final int DELAY_FIXED = 1;
	private static final int DELAY_RANDOM = 2;
	
	private long getFixedDelayComponentNanos(long timeout, TimeUnit unit) {
		return DELAY_FIXED;
	}

	class Account {
		public Lock lock;

		void debit(DollarAmount d) {

		}

		void credit(DollarAmount d) {

		}

		DollarAmount getBalance() {
			return null;
		}
	}

	static class DollarAmount implements Comparable<DollarAmount> {

		@Override
		public int compareTo(DollarAmount o) {
			return 0;
		}

		DollarAmount(int dollars) {

		}
	}
	
	class InsufficientFundException extends Exception {
		
	}
}
