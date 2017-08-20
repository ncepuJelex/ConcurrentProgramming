package net.jcip.examples.ch04;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import net.jcip.annotations.NotThreadSafe;
import net.jcip.annotations.ThreadSafe;

/**
 * 通过继承来增加功能到已有的线程安全类中
 * @author zhenhua
 * @date 2017年8月14日
 */
@ThreadSafe
public class BetterVector<E> extends Vector<E>{
	//当继承一个可序列化的类，你应该重新定义serialVersionUID
	private static final long serialVersionUID = 9143336221474999048L;

	public synchronized boolean putIfAbsent(E x) {
		boolean absent = !contains(x);
		if(absent) {
			add(x);
		}
		return absent;
	}
}

@NotThreadSafe
class BadListHelper<E> {
	public List<E> list = Collections.synchronizedList(new ArrayList<>());
	
	public synchronized boolean putIfAbsent(E x) {
		boolean absent = !list.contains(x);
		if(absent) {
			list.add(x);
		}
		return absent;
	}
}

@ThreadSafe
class GoodListHelper<E> {
	public List<E> list = Collections.synchronizedList(new ArrayList<>());
	
	public boolean putIfAbsent(E x) {
		synchronized(list) {
			boolean absent = !list.contains(x);
			if(absent) {
				list.add(x);
			}
			return absent;
		}
	}
}



