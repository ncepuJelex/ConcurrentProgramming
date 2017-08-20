package net.jcip.examples.ch02;

import java.io.IOException;
import java.math.BigInteger;

import javax.servlet.GenericServlet;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

/**
 * 线程安全的同时效率也不错！
 * 这里使用了2个局部synchronized代码块，把需要做原子操作的
 * 部分锁起来，这里是hit和lastNumber和lastFactors3个变量需要上锁操作。
 * @author zhenhua
 * @date 2017年8月13日
 */
@ThreadSafe
public class CachedFactorizer extends GenericServlet implements Servlet {

	private static final long serialVersionUID = -8189663149071300938L;

	@GuardedBy("this")
	private BigInteger lastNumber;
	@GuardedBy("this")
	private BigInteger [] lastFactors;
	@GuardedBy("this")
	private long hits;
	@GuardedBy("this")
	private long cacheHits;
	
	public synchronized long getHits() {
		return hits;
	}
	
	public synchronized double getCacheHitRatio() {
		return (double)cacheHits / (double)hits;
	}
	
	@Override
	public void service(ServletRequest req, ServletResponse resp) throws ServletException, IOException {
		
		BigInteger i = extractFromRequest(req);
		BigInteger [] factors = null;
		synchronized (this) {
			hits ++;
			if(i.equals(lastNumber)) {
				cacheHits ++;
				factors = lastFactors.clone();
			}
		}
		if(factors == null) {
			factors = factor(i);
			synchronized(this) {
				lastNumber = i;
				lastFactors = factors.clone();
			}
		}
		encodeIntoResponse(resp,factors);
	}

	private void encodeIntoResponse(ServletResponse resp, BigInteger[] factors) {
	}

	private BigInteger[] factor(BigInteger i) {
		return null;
	}

	private BigInteger extractFromRequest(ServletRequest req) {
		return null;
	}

}
