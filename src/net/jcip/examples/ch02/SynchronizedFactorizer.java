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
public class SynchronizedFactorizer extends GenericServlet implements Servlet {

	private static final long serialVersionUID = 7892255750182950270L;

	@GuardedBy("this")
	private BigInteger lastNumber;
	
	@GuardedBy("this")
	private BigInteger [] lastFactors;
	
	@Override
	public synchronized void service(ServletRequest req, ServletResponse resp) throws ServletException, IOException {
		
		BigInteger i = extractFromRequest(req);
		if(i.equals(lastNumber)) {
			encodeIntoResponse(resp,lastFactors);
		} else {
			BigInteger [] factors = factor(i);
			lastNumber = i;
			lastFactors = factors;
			encodeIntoResponse(resp,factors);
		}
	}

	private BigInteger[] factor(BigInteger i) {
		// TODO Auto-generated method stub
		return null;
	}

	private void encodeIntoResponse(ServletResponse resp, BigInteger[] lastFactors2) {
		// TODO Auto-generated method stub
		
	}

	private BigInteger extractFromRequest(ServletRequest req) {
		// TODO Auto-generated method stub
		return null;
	}

}
