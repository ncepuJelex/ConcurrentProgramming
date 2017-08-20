package net.jcip.examples.ch03;

import java.util.HashSet;
import java.util.Set;

/**
 * 你可真大方，secret都放到public static了！不泄露才怪！
 * @author zhenhua
 * @date 2017年8月13日
 */
public class Secrets {

	public static Set<Secret> knownSecrets;
	
	public void initialize() {
		knownSecrets = new HashSet<>();
	}
}

class Secret {
	
}
