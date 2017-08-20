package net.jcip.examples.ch07;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Uncaught Exception Handler,
 * 如果程序出毛病了，线程也调度某一个线程的时候，挂了，这个handler就派上用场了。
 * 在一个运行很长时间的应用中，记得使用Thread类中的这个handler接口，至少
 * 记录下exception.
 * @author Jelex.xu
 * @date 2017年8月20日
 */
public class UEHLogger implements Thread.UncaughtExceptionHandler {

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		Logger logger = Logger.getAnonymousLogger();
		logger.log(Level.SEVERE, "Thread terminated with exception:" + t.getName());
	}

}
