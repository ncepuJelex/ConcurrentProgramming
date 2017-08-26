package net.jcip.examples.ch10;

/**
 * 互相“拥抱”产生的死锁，想起了第一次看到是马士兵老师
 * 当年授课时举的例子，时间一晃啊！那时间还是大二，都5年了！
 * @author Jelex.xu
 * @date 2017年8月26日
 */
public class LeftRightDeadlock {
	private final Object left = new Object();
	private final Object right = new Object();
	
	public void leftRight() {
		synchronized(left) {
			synchronized(right) {
				doSomething();
			}
		}
	}

	public void rightLeft() {
		synchronized(right) {
			synchronized(left) {
				doSomethingElse();
			}
		}
	}
	
	private void doSomethingElse() {
	}

	private void doSomething() {
	}
}
