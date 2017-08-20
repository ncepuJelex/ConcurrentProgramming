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

/**
 * 这是线程安全的！因为它使用了一个AtomicLong变量，
 * 这样在count递增的操作是原子性的，不会存在线程不安全问题。
 * @author zhenhua
 * @date 2017年8月13日
 */
@ThreadSafe
public class CountingFactorizer extends GenericServlet implements Servlet {

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
	}

	private BigInteger [] factor(BigInteger i) {
		return null;
	}

	private BigInteger extractFromRequest(ServletRequest req) {
		return null;
	}

}
