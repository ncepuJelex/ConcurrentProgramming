package net.jcip.examples.ch05;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * 第3代缓存好些了，虽然还有些time leaving的情况，
 * 但概念比2代小多了，缓存中没有保存V,而是Future<V>,
 * 不是判断缓存中有没有，而是发现缓存中没有想要的数据时，
 * 缓存中有没有启动任务去开始计算。
 * 但是，缓存的失效时间问题，缓存大小问题还没有解决。
 * @author zhenhua
 * @date 2017年8月16日
 */
public class Memoizer3<A,V> implements Computable<A,V>{

	private final Map<A,Future<V>> cache = new ConcurrentHashMap<A,Future<V>>();
	
	private final Computable<A,V> c;
	
	public Memoizer3(Computable<A,V> c) {
		this.c = c;
	}
	
	@Override
	public V compute(A arg) throws InterruptedException {
		Future<V> f = cache.get(arg);
		if(f== null) {
			Callable<V> eval = new Callable<V>() {
				@Override
				public V call() throws Exception {
					return c.compute(arg);
				}
				
			};
			FutureTask<V> ft = new FutureTask<V>(eval);
			f = ft;//看下面f.get()就知道为什么这里赋值了
			cache.put(arg, ft);
			ft.run(); //call to c.compute happens here
		}
		
		try {
			return f.get();
		} catch (ExecutionException e) {
			throw LaunderThrowable.launderThrowable(e.getCause());
		}
	}

	
}
