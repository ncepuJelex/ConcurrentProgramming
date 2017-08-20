package net.jcip.examples.ch03;

import net.jcip.annotations.NotThreadSafe;

/**
 * 使用public来修饰Holder，这样发布一个变量不好啊！
 * @author zhenhua
 * @date 2017年8月14日
 */
@NotThreadSafe
public class StuffIntoPublic {

	public Holder holder;
	
	public void initialize() {
		holder = new Holder(42);
	}
	
	class Holder {
		
		private int num;
		
		public Holder(int num) {
			this.num = num;
		}
	}
}
