package net.jcip.examples.ch11;

/**
 * clear并不会实时地清除每个元素，因为上锁的时机，
 * 说不定你前脚刚清除掉这个bucket,去锁定下个bucket时，
 * 上个bucket又有东西放进去了！没办法，这就是cocurrent要
 * 付出的代价。
 * @author Jelex.xu
 * @date 2017年8月28日
 */
public class StripedMap {

	private static final int N_LOCKS = 16;
	private final Node[] buckets;
	private final Object[] locks;
	
	private class Node {
		Node next;
		Object key;
		Object value;
	}

	public StripedMap(int numBuckets) {
		this.buckets = new Node[numBuckets];
		locks = new Object[N_LOCKS];
		for(int i=0; i<N_LOCKS; i++) {
			locks[i] = new Object();
		}
	}
	
	private final int hash(Object key) {
		return Math.abs(key.hashCode() % buckets.length);
	}
	
	public Object get(Object key) {
		int hash = hash(key);
		synchronized(locks[hash % N_LOCKS]) {
			for(Node m = buckets[hash]; m!=null; m=m.next) {
				if(m.key.equals(key)) {
					return m.value;
				}
			}
		}
		return null;
	}
	
	public void clear() {
		for(int i=0; i<buckets.length; i++) {
			synchronized(locks[ i % N_LOCKS]) {
				buckets[i] = null;
			}
		}
	}
}
