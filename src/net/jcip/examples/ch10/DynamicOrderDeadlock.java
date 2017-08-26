package net.jcip.examples.ch10;

import java.util.concurrent.atomic.AtomicInteger;

public class DynamicOrderDeadlock {

	//warning deadlock-prone
	public static void transferMoney(Account fromAccount, Account toAccount, DollarAmount amount)
			throws InsufficientFundsException {
		
		synchronized(fromAccount) {
			synchronized(toAccount) {
				if(fromAccount.getBalance().compareTo(amount) < 0) {
					throw new InsufficientFundsException();
				}
				else {
					fromAccount.debit(amount);
					toAccount.credit(amount);
				}
			}
		}
	}
	
	static class Account {
		
		private DollarAmount balance = new DollarAmount(1000);
		private final int accNo;
		private static final AtomicInteger sequence = new AtomicInteger();
		
		public Account() {
			this.accNo = sequence.incrementAndGet();
		}
		
		void debit(DollarAmount d) {
			//balance = balance.subtract(d);
		}
		
		void credit(DollarAmount d) {
			//balance = balance.add(d);
		}

		public DollarAmount getBalance() {
			return balance;
		}

		public int getAccNo() {
			return accNo;
		}
		
	}
	
	static class DollarAmount implements Comparable<DollarAmount>{

		private int amount;
		
		public DollarAmount(int amount) {
			this.amount = amount;
		}
		
		public DollarAmount add(DollarAmount d) {
//			this.amount += d.amount;
//			return this;
			return null;
		}
		
		public DollarAmount subtract(DollarAmount d) {
			return null;
		}
		
		@Override
		public int compareTo(DollarAmount o) {
			return 0;
		}
	}
	
	static class InsufficientFundsException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
}
