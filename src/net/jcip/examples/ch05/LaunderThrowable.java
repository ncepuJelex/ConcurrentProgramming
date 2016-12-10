package net.jcip.examples.ch05;

/**
 * 
 * @author zhenhua
 *
 */
public class LaunderThrowable {

	/**
	 * if the Throwable is an Error, throw it;
	 * if it is a RuntimeException return it;
	 * otherwise throw IllegalStateException
	 */
	public static RuntimeException launderThrowable(Throwable t) {
		if( t instanceof RuntimeException) {
			return(RuntimeException) t;
		}
		else if(t instanceof Error) {
			throw (Error) t;
		}
		else {
			throw new IllegalStateException(t);
		}
	}
	
}

