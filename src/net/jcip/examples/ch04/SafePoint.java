package net.jcip.examples.ch04;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

/**
 * 正常的2个参数的构造方法提供，也提供一个传入javabean的构造，
 * 但不管怎么搞，最后都得是2个synchrinized方法来做事！安全！
 * 这种方式来把x,y组合变量绑定在一起完成原子性的操作。好
 * @author zhenhua
 * @date 2017年8月14日
 */
@ThreadSafe
public class SafePoint {

	@GuardedBy("this")
	private int x, y;
	
	private SafePoint(int [] a) {
		this(a[0],a[1]);
	}
	
	public SafePoint(SafePoint p) {
		this(p.get());
	}
	
	public SafePoint(int x ,int y) {
		this.set(x, y);
	}
	//这里上锁了
	public synchronized int [] get() {
		return new int[]{x,y};
	}
	//这里也上锁了
	public synchronized void set(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	
}
