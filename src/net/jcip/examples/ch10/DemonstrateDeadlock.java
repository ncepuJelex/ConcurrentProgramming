package net.jcip.examples.ch10;

import java.util.Random;

import net.jcip.examples.ch10.DynamicOrderDeadlock.Account;
import net.jcip.examples.ch10.DynamicOrderDeadlock.DollarAmount;
import net.jcip.examples.ch10.DynamicOrderDeadlock.InsufficientFundsException;

/**
 * 卡住了，stuck,ha~看见死锁就对了！
 * 如果我把NUM_ITERATIONS 修改为数量1，就不会死锁了，但是生产
 * 环境上就得是1000000这种级别的！
 * @author Jelex.xu
 * @date 2017年8月26日
 */
public class DemonstrateDeadlock {

	private static final int NUM_THREADS = 20;
	private static final int NUM_ACCOUNTS = 5;
//	private static final int NUM_ITERATIONS = 1000000;
	private static final int NUM_ITERATIONS = 1;
	
	public static void main(String [] args) {
		final Random rnd = new Random();
		final Account [] accounts = new Account[NUM_ACCOUNTS];
		
		for(int i=0; i<accounts.length; i++) {
			accounts[i] = new Account();
		}
		
		class TransferThread extends Thread {
			@Override
			public void run() {
				for(int i=0; i<NUM_ITERATIONS; i++) {
					int fromAcct = rnd.nextInt(NUM_ACCOUNTS);
					int toAcct = rnd.nextInt(NUM_ACCOUNTS);
					DollarAmount amount = new DollarAmount(rnd.nextInt(1000));
					
					try {
						DynamicOrderDeadlock.transferMoney(accounts[fromAcct], accounts[toAcct], amount);
					} catch (InsufficientFundsException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		for(int i=0; i<NUM_THREADS; i++) {
			new TransferThread().start();
		}
	}
}
