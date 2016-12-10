package net.jcip.examples.ch02;

public class LoggingWidget extends Widget {

	public synchronized void doSomething() {
		System.out.println(toString() + ": doing something");
		super.doSomething();
	}
}
