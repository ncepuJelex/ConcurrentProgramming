package net.jcip.examples.ch05;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import net.jcip.annotations.GuardedBy;

/**
 * 这也算是一个初级的缓存吧！
 * 但是并发方面好差啊！
 * @author zhenhua
 * @date 2017年8月16日
 */
public class Memoizer1<A,V> implements Computable<A,V> {

	@GuardedBy("this")
	private final Map<A,V> cache = new HashMap<>();
	
	private final Computable<A,V> c;
	
	public Memoizer1(Computable<A,V> c) {
		this.c = c;
	}
	
	@Override
	public synchronized V compute(A arg) throws InterruptedException {
		V result = cache.get(arg);
		if(result == null) {
			result = c.compute(arg);
			cache.put(arg, result);
		}
		return result;
	}

	
}

interface Computable<A,V> {
	V compute(A arg) throws InterruptedException;
}

class ExpensiveFunction implements Computable<String,BigInteger> {

	@Override
	public BigInteger compute(String arg) throws InterruptedException {
		//after deep thought...
		return new BigInteger(arg);
	}
	
}
