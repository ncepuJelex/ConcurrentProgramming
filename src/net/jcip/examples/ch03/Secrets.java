package net.jcip.examples.ch03;

import java.util.HashSet;
import java.util.Set;

public class Secrets {

	public static Set<Secret> knownSecrets;
	
	public void initialize() {
		knownSecrets = new HashSet<>();
	}
}

class Secret {
	
}
