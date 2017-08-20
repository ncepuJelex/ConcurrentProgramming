package net.jcip.examples.ch07;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 这个写得感觉好low啊！好吧，是自己写的。
 * @author Jelex.xu
 * @date 2017年8月18日
 */
public class LogService2 {

	private static final TimeUnit UNIT = TimeUnit.SECONDS;
	private static final long TIMEOUT = 10;
	
	private final PrintWriter writer;
	
	private final ExecutorService exec = Executors.newSingleThreadExecutor();
	
	public LogService2(Writer writer) {
		this.writer = new PrintWriter(writer);
	}

	public void start() {
	}
	
	public void stop() throws InterruptedException {
		try {
			exec.shutdown();
			exec.awaitTermination(TIMEOUT, UNIT);
		} finally {
			writer.close();
		}
	}
	
	public void log(String msg) throws InterruptedException {
		try {
			exec.execute(new LoggerTask(msg));
		} catch(RejectedExecutionException egnored) {}
	}

	private class LoggerTask extends Thread {
		
		private String msg;
		
		public LoggerTask(String msg) {
			this.msg = msg;
		}

		@Override
		public void run() {
			try {
				writer.println(msg);
			} finally {
				writer.close();
			}
		}
	}
	
}
