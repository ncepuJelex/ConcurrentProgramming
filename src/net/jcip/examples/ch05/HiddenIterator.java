package net.jcip.examples.ch05;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.NotThreadSafe;

@NotThreadSafe
public class HiddenIterator {

	@GuardedBy("this")
	private final Set<Integer> set = new HashSet<Integer>();
	
	public synchronized void add(Integer i) {
		set.add(i);
	}
	
	public synchronized void remove(Integer i) {
		set.remove(i);
	}
	
	public void addTenThings() {
		Random r = new Random();
		for(int i=0; i<10; i++) {
			add(r.nextInt());
		}
		//内部调用 了toString()方法，toString()使用了循环遍历，这不ok啊
		System.out.println("DEBUG:added ten elements to " + set);
	}
}
