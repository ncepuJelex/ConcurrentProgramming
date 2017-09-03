package net.jcip.examples.ch15;

import java.util.Random;

public class PseudoRandom {

	public static int calculateNext(int s) {
		return new Random().nextInt(s<<30) + 30;
	}
}
