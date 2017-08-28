package net.jcip.examples.ch11;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import net.jcip.annotations.GuardedBy;
/**
 * 如类名所述，这是一个better 使用锁的例子。
 * @author Jelex.xu
 * @date 2017年8月27日
 */
public class BetterAttributeStore {

	@GuardedBy("this")
	private final Map<String, String> attributes = new HashMap<String, String>();
	
	public boolean userLocationMatchers(String name, String regexp) {
		String key = "users." + name + ".location";
		String location;
		synchronized(this) {
			location = attributes.get(key);
		}
		if(location == null) {
			return false;
		} else {
			return Pattern.matches(regexp, location);
		}
	}
}
