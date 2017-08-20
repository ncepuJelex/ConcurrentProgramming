package net.jcip.examples.ch07;

import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 记录日志，生产者消费者模式，多个生产者，多个类(地方)
 * 记住日志，而只有这里面的一个LoggerThread消费(print到某个地方)
 * 这个工具还不能应用于生产环境中的意义在于：无法关闭LoggerThread,
 * 阻止JVM退出，看后来的优化吧！
 * @author Jelex.xu
 * @date 2017年8月18日
 */
public class LogWriter {

	private static final int CAPACITY = 1000;
	
	private final BlockingQueue<String> queue;
	private final LoggerThread logger;
	
	public LogWriter(PrintWriter writer) {
		this.queue = new LinkedBlockingQueue<String>(CAPACITY);
		this.logger = new LoggerThread(writer);
	}

	public void start() {
		logger.start();
	}
	
	//这里为什么直接就抛出异常了啊？而不是try catch住
	public void log(String msg) throws InterruptedException {
		queue.put(msg);
	}
	

	private class LoggerThread extends Thread {
		//PrintWriter is thread safe.
		private final PrintWriter writer;

		public LoggerThread(PrintWriter writer) {
			this.writer = writer;
		}

		@Override
		public void run() {
			try {
				while (true) {
					writer.println(queue.take());
				} 
			} catch (Exception ignored) {
			} finally {
				writer.close();
			}
		}
		
		
		
	}
}
