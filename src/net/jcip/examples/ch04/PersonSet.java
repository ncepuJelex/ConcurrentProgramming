package net.jcip.examples.ch04;

import java.util.HashSet;
import java.util.Set;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

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
