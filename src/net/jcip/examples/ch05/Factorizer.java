package net.jcip.examples.ch05;

import java.io.IOException;
import java.math.BigInteger;

import javax.servlet.GenericServlet;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class Factorizer extends GenericServlet implements Servlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -119631412152619351L;

	private final Computable<BigInteger, BigInteger[]> c = 
			new Computable<BigInteger, BigInteger[]>() {
				@Override
				public BigInteger[] compute(BigInteger arg) throws InterruptedException {
					return factor(arg);
				}
			};
	
	private final Computable<BigInteger,BigInteger[]> cache = new Memoizer<BigInteger,BigInteger[]>(c);
	
	
	@Override
	public void service(ServletRequest req, ServletResponse resp) throws ServletException, IOException {

		try {
			BigInteger i = extractFromRequest(req);
			encodeIntoResponse(resp,cache.compute(i));
		} catch (InterruptedException e) {
			encodeError(resp,"factorization interrupted...");
		}
		
	}
	
	private void encodeError(ServletResponse resp, String string) {
		
	}

	private void encodeIntoResponse(ServletResponse resp, BigInteger[] compute) {
		
	}

	private BigInteger extractFromRequest(ServletRequest req) {
		return new BigInteger("7");
	}

	private BigInteger[] factor(BigInteger i) {
		//Doesn't really factor
		return new BigInteger[]{i};
	}
}
