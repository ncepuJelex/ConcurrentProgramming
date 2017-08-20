package net.jcip.examples.ch07;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import net.jcip.annotations.GuardedBy;

/**
 * 这个记录日志的可以，有取消线程的功能。
 * @author Jelex.xu
 * @date 2017年8月18日
 */
public class LogService {

	private final BlockingQueue<String> queue;
	private final LoggerThread logger;
	private final PrintWriter writer;
	
	@GuardedBy("this") private boolean isShutdown;
	@GuardedBy("this") private int reservations;
	
	public LogService(Writer writer) {
		this.queue = new LinkedBlockingQueue<String>();
		this.logger = new LoggerThread();
		this.writer = new PrintWriter(writer);
	}

	public void start() {
		logger.start();
	}
	
	public void stop() {
		synchronized(this) {
			isShutdown = true;
		}
		logger.interrupt();
	}
	
	public void log(String msg) throws InterruptedException {
		synchronized(this) {
			if(isShutdown) {
				throw new IllegalStateException("log service is already shutdown.");
			}
			reservations++;
		}
		queue.put(msg);
	}

	
	private class LoggerThread extends Thread {
		
		@Override
		public void run() {
			try {
				while(true) {
					synchronized(LogService.this) {
						if(isShutdown && reservations == 0) {
							break;
						}
					}
					String msg = queue.take();
					synchronized(LogService.this) {
						reservations--;
					}
					writer.println(msg);
				}
			} catch(InterruptedException ignored) {
			} finally {
				writer.close();
			}
		}
		
	}
	
}
