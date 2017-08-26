package net.jcip.examples.ch10;

/**
 * 引入lock的顺序来避免deadlock
 * @author Jelex.xu
 * @date 2017年8月26日
 */
public class InduceLockOrder {

	//当hashcode竟然还会一致时使用
	private static final Object tieLock = new Object();
	
	public void transferMoney(Account fromAccount, Account toAccount,
			DollarAmount d) throws InsufficientFundsException {
		//内部辅助类
		class Helper {
			public void transfer() throws InsufficientFundsException {
				if(fromAccount.getBalance().compareTo(d) < 0) {
					throw new InsufficientFundsException();
				} else {
					fromAccount.debit(d);
					toAccount.credit(d);
				}
			}
		}
		/*
		 * 还能这样用！
		 */
		int fromHash = System.identityHashCode(fromAccount);
		int toHash = System.identityHashCode(toAccount);
		
		if(fromHash < toHash) {
			synchronized(fromAccount) {
				synchronized(toAccount) {
					new Helper().transfer();
				}
			}
		} else if(fromHash > toHash) {
			synchronized(toAccount) {
				synchronized(fromAccount) {
					new Helper().transfer();
				}
			}
		} else { //这种情况太TM少见了！
			synchronized(tieLock) {
				synchronized(fromAccount) {
					synchronized(toAccount) {
						new Helper().transfer();
					}
				}
			}
		}
	}
	
	interface Account {
		void debit(DollarAmount d);
		void credit(DollarAmount d);
		DollarAmount getBalance();
		int getAccNo();
	}
	
	interface DollarAmount extends Comparable<DollarAmount> {
		
	}
	class InsufficientFundsException extends Exception {
		/**
		 * 
		 */
		private static final long serialVersionUID = 3296397628954126284L;
	}
}
