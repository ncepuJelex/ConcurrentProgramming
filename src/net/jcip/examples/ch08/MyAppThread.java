package net.jcip.examples.ch08;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 设置线程名称，加上日志，乱七八糟的。
 * @author Jelex.xu
 * @date 2017年8月21日
 */
public class MyAppThread extends Thread {

	public static final String DEFAULT_NAME = "MyAppThread";
	
	private static volatile boolean debugLifeCycle = false;
	
	private static final AtomicInteger created = new AtomicInteger();
	private static final AtomicInteger alive = new AtomicInteger();
	
	private static final Logger log = Logger.getAnonymousLogger();

	public MyAppThread(Runnable r) {
		this(r, DEFAULT_NAME);
	}
	public MyAppThread(Runnable r, String name) {
		super(r, name + "-" + created.incrementAndGet());
		this.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				log.log(Level.SEVERE, "UNCAUGHT in thread " + t.getName(), e);
			}
		});
	}
	
	@Override
	public void run() {
		//copy debug flag to ensure Consistent value throughout
		boolean debug = debugLifeCycle;
		if(debug) {
			log.log(Level.FINE, "Created " + getName());
		}
		try {
			alive.incrementAndGet();
			super.run();
		} finally {
			alive.decrementAndGet();
			if(debug) log.log(Level.FINE, "Exiting " + getName());
		}
	}
	
	
	public static boolean isDebugLifeCycle() {
		return debugLifeCycle;
	}

	public static void setDebugLifeCycle(boolean debugLifeCycle) {
		MyAppThread.debugLifeCycle = debugLifeCycle;
	}

	public static int getCreated() {
		return created.get();
	}

	public static int getAlive() {
		return alive.get();
	}
	
	
}
