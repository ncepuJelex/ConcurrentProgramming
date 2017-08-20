package net.jcip.examples.ch02;

import net.jcip.annotations.NotThreadSafe;

/**
 * race conditon in lazy initialization,
 * 在某个time leave的时刻，instance == null,
 * 然后new的时候其实已经不为空了，在这个time leave点
 * 已经初始化了，哎！线程不安全的样例，check then act.
 * @author zhenhua
 * @date 2017年8月13日
 */
@NotThreadSafe
public class LazyInitRace {

	private ExpensiveObject instance = null;
	
	public ExpensiveObject getInstance() {
		if(instance == null) {
			instance = new ExpensiveObject();
		}
		return instance;
	}
	
}

class ExpensiveObject {}
