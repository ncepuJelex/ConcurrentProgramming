package net.jcip.examples.ch13;

/**
 * 试一下抛出RuntimeException时会不会走finally 代码，
 * 会走！
 * @author Jelex.xu
 * @date 2017年8月30日
 */
public class TestRuntimeExceptionWithFinally {

	public static void main(String[] args) {
		try {
			int a = 10;
			System.out.println(a);
			if (a > 9) {
				throw new IllegalArgumentException();
			} 
		} finally {
			System.out.println("finally..."); //it will goes to here.没毛病~
		}
	}
}
