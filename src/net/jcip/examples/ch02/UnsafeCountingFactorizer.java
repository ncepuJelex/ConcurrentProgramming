package net.jcip.examples.ch02;

import java.io.IOException;
import java.math.BigInteger;

import javax.servlet.GenericServlet;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import net.jcip.annotations.NotThreadSafe;

/**
 * 非线程安全，因为引入了一个成员变量 count,
 * 当多个线程访问这个变量时候，没有做原子操作，
 * 所以有线程不安全问题
 * @author zhenhua
 * @date 2017年8月13日
 */
@NotThreadSafe
public class UnsafeCountingFactorizer extends GenericServlet implements Servlet {

	private static final long serialVersionUID = -423556606935292813L;

	private long count = 0;
	
	public long getCount() {
		return count;
	}

	@Override
	public void service(ServletRequest req, ServletResponse resp) throws ServletException, IOException {

		BigInteger i = extractFromRequest(req);
		BigInteger[] factors = factor(i);
		count ++;
		encodeIntoResponse(resp, factors);
	}
	
	private void encodeIntoResponse(ServletResponse resp, BigInteger[] factors) {
	}

	private BigInteger[] factor(BigInteger i) {
		return new BigInteger[]{i};
	}

	private BigInteger extractFromRequest(ServletRequest req) {
		return new BigInteger("7");
	}

}
