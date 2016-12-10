package net.jcip.examples.ch03;

import java.io.IOException;
import java.math.BigInteger;

import javax.servlet.GenericServlet;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import net.jcip.annotations.ThreadSafe;

@ThreadSafe
public class VolatileCachedFactorizer extends GenericServlet implements Servlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6931410355603668761L;

	private volatile OneValueCache cache = new OneValueCache(null,null);
	
	@Override
	public void service(ServletRequest req, ServletResponse resp) throws ServletException, IOException {

		BigInteger i = extractFromRequest(req);
		BigInteger [] factors = cache.getFactors(i);
		
		if(factors == null) {
			factors = factor(i);
			cache = new OneValueCache(i,factors);
		}
		encodeIntoResponse(resp,factors);
	}

	private void encodeIntoResponse(ServletResponse resp, BigInteger[] factors) {
		// TODO Auto-generated method stub
		
	}

	private BigInteger [] factor(BigInteger i) {
		// TODO Auto-generated method stub
		return null;
	}

	private BigInteger extractFromRequest(ServletRequest req) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
