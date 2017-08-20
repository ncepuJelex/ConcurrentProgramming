package net.jcip.examples.ch04;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

/**
 * 深度复制，虽然MutablePoint不是线程安全的，但是这个Tracker类是啊！
 * @author zhenhua
 * @date 2017年8月14日
 */
@ThreadSafe
public class MonitorVehicleTracker {

	@GuardedBy("this")
	private final Map<String, MutablePoint> locations;
	
	public MonitorVehicleTracker(Map<String, MutablePoint> locations) {
		this.locations = deeplyCopy(locations);
	}
	
	public synchronized Map<String,MutablePoint> getLocations() {
		return deeplyCopy(locations);
	}
	
	public synchronized MutablePoint getLocation(String id) {
		MutablePoint loc = locations.get(id);
		return loc == null ? null : new MutablePoint(loc);
	}
	
	public synchronized void setLocation(String id, int x ,int y) {
		MutablePoint loc = locations.get(id);
		if(loc == null) {
			throw new IllegalArgumentException("No such ID: " + id);
		}
		loc.x = x;
		loc.y = y;
	}

	private Map<String, MutablePoint> deeplyCopy(Map<String, MutablePoint> m) {
		Map<String,MutablePoint> result = new HashMap<>();
		for(String id : m.keySet()) {
			result.put(id, new MutablePoint(m.get(id)));
		}
		return Collections.unmodifiableMap(result);
	}
}
