package net.jcip.examples.ch03;

import net.jcip.annotations.NotThreadSafe;

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
