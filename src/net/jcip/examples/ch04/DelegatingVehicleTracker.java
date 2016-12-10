package net.jcip.examples.ch04;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.jcip.annotations.ThreadSafe;

@ThreadSafe
public class DelegatingVehicleTracker {

	private final ConcurrentHashMap<String,Point> locations;
	private final Map<String, Point> unmodifiableMap;
	
	public DelegatingVehicleTracker(Map<String,Point> points) {
		locations = new ConcurrentHashMap<String,Point>(points);
		unmodifiableMap = Collections.unmodifiableMap(points);
	}
	
	public Map<String, Point> getLocations() {
		return unmodifiableMap;
	}
	
	public Point getLocation(String id) {
		return locations.get(id);
	}
	
	public void setLocation(String id, int  x ,int y) {
		if(locations.replace(id, new Point(x,y))==null) {
			throw new IllegalArgumentException("invalid vehicle name" + id);
		}
	}
	
	public Map<String, Point> getLocationsAsStatic() {
		return Collections.unmodifiableMap(new HashMap<String, Point>(locations));
	}
}
