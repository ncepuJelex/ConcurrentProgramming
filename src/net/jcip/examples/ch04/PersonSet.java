package net.jcip.examples.ch04;

import java.util.HashSet;
import java.util.Set;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

/**
 * private 修饰的personSet变量，
 * 而能访问到它的只有下面2个上锁的方法，这是ok的！
 * @author zhenhua
 * @date 2017年8月14日
 */
@ThreadSafe
public class PersonSet {

	@GuardedBy("this")
	private final Set<Person> personSet = new HashSet<>();
	
	public synchronized void addPerson(Person p) {
		personSet.add(p);
	}
	
	public synchronized boolean containsPerson(Person p) {
		return personSet.contains(p);
	}
	
	interface Person {
		
	}
}
