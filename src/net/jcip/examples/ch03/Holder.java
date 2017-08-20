package net.jcip.examples.ch03;

/**
 * 不可思议吧！n != n 竟然有时候不成立！都是线程不安全惹的祸！
 * @author zhenhua
 * @date 2017年8月14日
 */
public class Holder {

	private int n;
	
	public Holder(int n) {
		this.n = n;
	}
	
	public void assertSanity() {
		if(n != n) {
			throw new AssertionError("This statement is false...");
		}
	}
}
