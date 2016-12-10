package net.jcip.examples.ch04;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.jcip.annotations.ThreadSafe;

@ThreadSafe
public class PublishingVehicleTracker {

	private final Map<String, SafePoint> locations;
	private final Map<String, SafePoint> unmodifiableMap;
	
	public PublishingVehicleTracker(Map<String, SafePoint> points) {
		locations = new ConcurrentHashMap<String,SafePoint>(points);
		unmodifiableMap = Collections.unmodifiableMap(this.locations);
	}
	
	public Map<String, SafePoint> getLocations() {
		return unmodifiableMap;
	}
	
	public SafePoint getLocation(String id) {
		return locations.get(id);
	}
	
	public void setLocation(String id, int x, int y) {
		if(!locations.containsKey(id)) {
			throw new IllegalArgumentException("invalid vehicle name :" + id);
		}
		locations.get(id).set(x, y);
	}
}
