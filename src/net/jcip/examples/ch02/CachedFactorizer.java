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

@ThreadSafe
public class CachedFactorizer extends GenericServlet implements Servlet {

	/**
	 * 
	 */
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
		// TODO Auto-generated method stub
		
	}

	private BigInteger[] factor(BigInteger i) {
		// TODO Auto-generated method stub
		return null;
	}

	private BigInteger extractFromRequest(ServletRequest req) {
		// TODO Auto-generated method stub
		return null;
	}

}
