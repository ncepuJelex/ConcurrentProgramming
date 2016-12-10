package net.jcip.examples.ch02;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.GenericServlet;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import net.jcip.annotations.ThreadSafe;

@ThreadSafe
public class CountingFactorizer extends GenericServlet implements Servlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5373176975167112124L;
	
	private final AtomicLong count = new AtomicLong(0);
	
	public AtomicLong getCount() {
		return count;
	}

	@Override
	public void service(ServletRequest req, ServletResponse resp) throws ServletException, IOException {

		BigInteger i = extractFromRequest(req);
		BigInteger [] factors= factor(i);
		count.getAndIncrement();
		encodeIntoResponse(resp,factors);
	}

	private void encodeIntoResponse(ServletResponse resp, BigInteger[] factors) {
		// TODO Auto-generated method stub
		
	}

	private BigInteger [] factor(BigInteger i) {
		return null;
	}

	private BigInteger extractFromRequest(ServletRequest req) {
		// TODO Auto-generated method stub
		return null;
	}

}
