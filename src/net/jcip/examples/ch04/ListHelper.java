package net.jcip.examples.ch04;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 使用client side lock来在已有的线程安全类上增加功能
 * @author zhenhua
 * @date 2017年8月14日
 */
public class ListHelper<E> {

	public List<E> list = Collections.synchronizedList(new ArrayList<E>());
	
	//...
	
	public boolean putIfAbsent(E x) {
		synchronized(list) { //注意这里，不要使用错了要上锁的对象啊！
			boolean absent = !list.contains(x);
			if(absent) {
				list.add(x);
			}
			return absent;
		}
	}
}
