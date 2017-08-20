package net.jcip.examples.ch02;

/**
 * 演示了锁的可重入性，reentrancy,
 * 这个类和Widget结合一起说明使用
 * @author zhenhua
 * @date 2017年8月13日
 */
public class LoggingWidget extends Widget {

	public synchronized void doSomething() {
		System.out.println(toString() + ": doing something");
		super.doSomething();
	}
}
