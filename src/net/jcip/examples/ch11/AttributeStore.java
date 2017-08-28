package net.jcip.examples.ch11;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import net.jcip.annotations.GuardedBy;
/*
 * 把整个方法都synchronized了，占用锁的时间太长了，其实只要
 * 锁住attributes.get(key)那一项的，所以你应该知道怎么
 * 通过 narrowing lock scope来reduce lock contention,
 * 最终来提高performance 和 scalability.
 */
public class AttributeStore {

	@GuardedBy("this")
	private final Map<String, String> attributes = new HashMap<String, String>();
	
	public synchronized boolean userLocationMatcher(String name, String regexp) {
		
		String key = "users." + name + ".location";
		String location = attributes.get(key);
		if(location == null) {
			return false;
		} else {
			return Pattern.matches(regexp, location);
		}
	}
}
