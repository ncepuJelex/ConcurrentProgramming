package net.jcip.examples.ch03;

/**
 * 虽然states变量是private的，
 * 但是它有个get方法是public的，也会有escape问题
 * @author zhenhua
 * @date 2017年8月13日
 */
public class UnsafeStatus {

	private String [] states = new String[] {
		"AK","AL",
	};
	
	public String[] getStates() {
		return states;
	}
}
