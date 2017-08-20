package net.jcip.examples.ch02;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicReference;

import javax.servlet.GenericServlet;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import net.jcip.annotations.NotThreadSafe;

/**
 * 虽然使用了线程安全的类，AtomicReference,但程序不是线程安全的。
 * 因为存在race condition,要是把这2个变量的操作绑定在一起，
 * 成一个原子操作，那这个程序就是线程安全的了。
 * @author zhenhua
 * @date 2017年8月13日
 */
@NotThreadSafe
public class UnsafeCachingFactorizer extends GenericServlet implements Servlet {

	private static final long serialVersionUID = 385532239830913608L;

	private final AtomicReference<BigInteger> lastNumber = new AtomicReference<>();
	private final AtomicReference<BigInteger[]> lastFactors = new AtomicReference<>();
	
	
	@Override
	public void service(ServletRequest req, ServletResponse resp) throws ServletException, IOException {
		
		BigInteger i = extractFromRequest(req);
		if(i.equals(lastNumber.get())) {
			encodeIntoResponse(resp,lastFactors.get());
		}
		else {
			BigInteger [] factors = factor(i);
			lastNumber.set(i);
			lastFactors.set(factors);
			encodeIntoResponse(resp,factors);
		}
	}

	private BigInteger[] factor(BigInteger i) {
		return new BigInteger[]{i};
	}

	private void encodeIntoResponse(ServletResponse resp, BigInteger[] bigIntegers) {
		
	}

	private BigInteger extractFromRequest(ServletRequest req) {
		return new BigInteger("7");
	}

}
