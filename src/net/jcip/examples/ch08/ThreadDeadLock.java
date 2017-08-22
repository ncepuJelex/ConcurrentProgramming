package net.jcip.examples.ch08;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
/**
 * 会死锁的程序，因为多个任务不是各自独立的，而且还只有
 * 一个single-thread executor.
 * @author Jelex.xu
 * @date 2017年8月21日
 */
public class ThreadDeadLock {

	ExecutorService exec = Executors.newSingleThreadExecutor();
	
	public class LoadFileTask implements Callable<String> {

		private final String fileName;
		
		public LoadFileTask(String fileName) {
			this.fileName = fileName;
		}
		
		@Override
		public String call() throws Exception {
			//here is where we would actually read the file.
			return "";
		}
	}
	
	
	public class RendPageTask implements Callable<String> {

		@Override
		public String call() throws Exception {
			Future<String> header, footer;
			header = exec.submit(new LoadFileTask("header.html"));
			footer = exec.submit(new LoadFileTask("footer.html"));
			String page = renderBody();
			//死锁啦！这个任务会去等待header和footer的任务完成
			//但是因为只有一个线程调度执行，当执行你这个任务的时候，header和
			//footer的任务就没法做了，然后你在这能等到结果吗？
			return header.get() + page + footer.get();
		}
		
		private String renderBody() {
			//here is where we would actually render the page.
			return "";
		}
		
	}
}
