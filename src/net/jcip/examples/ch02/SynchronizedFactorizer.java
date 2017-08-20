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
 * 线程安全，但是程序很垃圾，非常低效！
 * 因为锁把整个方法都锁住了，这其实是没必要的！
 * @author zhenhua
 * @date 2017年8月13日
 */
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
		return null;
	}

	private void encodeIntoResponse(ServletResponse resp, BigInteger[] lastFactors2) {
		
	}

	private BigInteger extractFromRequest(ServletRequest req) {
		return null;
	}

}
