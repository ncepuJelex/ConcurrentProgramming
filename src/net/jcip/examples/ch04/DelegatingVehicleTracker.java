package net.jcip.examples.ch04;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.jcip.annotations.ThreadSafe;

/**
 * private final 变量修饰，注意哦，这里面使用到的Point中变量是final的，是
 * immutable的，而且看到的车子是live的，不是snapshot.
 * @author zhenhua
 * @date 2017年8月14日
 */
@ThreadSafe
public class DelegatingVehicleTracker {

	private final ConcurrentHashMap<String,Point> locations;
	private final Map<String, Point> unmodifiableMap;
	
	public DelegatingVehicleTracker(Map<String,Point> points) {
		locations = new ConcurrentHashMap<String,Point>(points);
		unmodifiableMap = Collections.unmodifiableMap(locations);
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
