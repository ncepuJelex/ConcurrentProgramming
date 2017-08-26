package net.jcip.examples.ch10;

import java.util.HashSet;
import java.util.Set;

import net.jcip.annotations.GuardedBy;
import net.jcip.examples.ch04.Point;

/**
 * 不易察觉的deadlock-prone,看吧！
 * @author Jelex.xu
 * @date 2017年8月26日
 */
public class CooperatingDeadlock {

	class Taxi {
		@GuardedBy("this") private Point location, destination;
		private final Dispatcher dispatcher;
		
		public Taxi(Dispatcher dispatcher) {
			this.dispatcher = dispatcher;
		}
		
		public synchronized Point getLocation() {
			return location;
		}
		
		public synchronized void setLocation(Point location) {
			this.location = location;
			if(location.equals(destination)) {
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
		
		public synchronized Image getImage() {
			Image img = new Image();
			for(Taxi t : taxis) {
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
