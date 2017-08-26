package net.jcip.examples.ch10;

import java.util.HashSet;
import java.util.Set;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import net.jcip.examples.ch04.Point;

public class CooperatingNoDeadlock {

	@ThreadSafe
	class Taxi {
		@GuardedBy("this") private Point location, destination;
		private final Dispatcher dispatcher;
		
		public Taxi(Dispatcher dispatcher) {
			this.dispatcher = dispatcher;
		}
		
		public synchronized Point getLocation() {
			return location;
		}
		
		public void setLocation(Point location) {
			boolean reachedDestination;
			synchronized(this) {
				this.location = location;
				reachedDestination = location.equals(destination);
			}
			if(reachedDestination) {
				dispatcher.notifyAvailable(this);
			}
		}

		public synchronized Point getDestination() {
			return destination;
		}

		public synchronized void setDestination(Point destination) {
			this.destination = destination;
		}
	}
	
	class Dispatcher {
		@GuardedBy("this") private final Set<Taxi> taxis;
		@GuardedBy("this") private final Set<Taxi> availableTaxis;
		
		public Dispatcher() {
			this.taxis = new HashSet<Taxi>();
			this.availableTaxis = new HashSet<Taxi>();
		}
		
		public synchronized void notifyAvailable(Taxi taxi) {
			availableTaxis.add(taxi);
		}
		
		public Image getImage() {
			Set<Taxi> copy;
			synchronized(this) {
				copy = new HashSet<Taxi>(taxis);
			}
			Image img = new Image();
			for(Taxi t : copy) {
				img.drawMarker(t.getLocation());
			}
			return img;
		}
	}
	
	class Image {
		public void drawMarker(Point p) {
		}
	}
}
