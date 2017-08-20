package net.jcip.examples.ch03;

import net.jcip.annotations.NotThreadSafe;

/**
 * 你以为synchronized功能只是保证操作上的原子性？
 * 其实还有可见性，对某个变量的可见性
 * @author zhenhua
 * @date 2017年8月13日
 */
@NotThreadSafe
public class NoVisibility {

	private static boolean ready;
	private static int number;
	
	private static class ReadThread extends Thread {

		@Override
		public void run() {
			while(!ready) {
				Thread.yield();
			}
			System.out.println(number);
		}
	}
	public static void main(String [] args) {
		new ReadThread().start();
		number = 19;
		ready = true;
	}
}
