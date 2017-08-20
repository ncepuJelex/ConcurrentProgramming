package net.jcip.examples.ch05;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 二代缓存，使用了ConcurrentHashMap,而不是
 * 像1代那样，使用HashMap，然后用syncrhonized把
 * compute(...)锁起来，效率好点。
 * 但是，在某些time leaving时刻，比如,a在计算中，
 * b来了，发现缓存中没有，也接着去计算，然后又重复计算了，
 * 缓存意义没有了。
 * @author zhenhua
 * @date 2017年8月16日
 */
public class Memoizer2<A,V> implements Computable<A, V> {

	private final Map<A,V> cache = new ConcurrentHashMap<>();
	private final Computable<A,V> c;
	
	public Memoizer2(Computable<A,V> c) {
		this.c = c;
	}
	
	@Override
	public V compute(A arg) throws InterruptedException {
		V result = cache.get(arg);
		if(result == null) {
			result = c.compute(arg);
			cache.put(arg, result);
		}
		return result;
	}

}
