package net.jcip.examples.ch03;

public class CountingSleep {

	volatile boolean asleep;
	
	void tryToSleep() {
		while(!asleep) {
			countSomeSheep();
		}
	}
	
	void countSomeSheep() {
		//one,two,three...
	}
}
