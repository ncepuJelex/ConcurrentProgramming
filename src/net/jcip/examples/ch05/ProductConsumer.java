package net.jcip.examples.ch05;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
/**
 * 好一个生产者和消费者模式！
 * 但是不仅仅是这个哦！还有FileFilter功能，还有线程方面的东西，
 * BlockQueue，它们操作的是一个对象，
 * 
 * @author zhenhua
 * @date 2017年8月15日
 */
public class ProductConsumer {

	static class FileCrawler implements Runnable {

		private final BlockingQueue<File> fileQueue;
		private final FileFilter fileFilter;
		private final File root;
		
		public FileCrawler(BlockingQueue<File> fileQueue, FileFilter fileFilter, File root) {
			this.fileQueue = fileQueue;
			this.fileFilter = new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					return pathname.isDirectory() || fileFilter.accept(pathname);
				}
			};
			this.root = root;
		}
		
		public boolean alreadyIndexed(File f) {
			return false;
		}
		
		@Override
		public void run() {
			try {
				crawl(root);
			} catch (InterruptedException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}

		private void crawl(File root) throws InterruptedException {
			File [] entries = root.listFiles(fileFilter);
			if(entries != null) {
				for(File entry : entries) {
					if(entry.isDirectory()) {
						crawl(entry);
					}
					else if(!alreadyIndexed(entry)) {
						fileQueue.put(entry);
					}
				}
			}
		}
		
	}
	
	static class Indexer implements Runnable {

		private final BlockingQueue<File> queue;
		
		public Indexer (BlockingQueue<File> queue) {
			this.queue = queue;
		}
		@Override
		public void run() {
			try {
				while(true) {
					indexFile(queue.take());
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
		private void indexFile(File take) {
			//Index the file...
			System.out.println(take.getAbsolutePath());
		}
		
	}
	
	private static final int BOUND = 10;
	private static final int N_CONSUMERS = Runtime.getRuntime().availableProcessors();
	
	public static void startIndexing(File[] roots) {
		
		BlockingQueue<File> queue = new LinkedBlockingQueue<>();
		FileFilter filter = new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return true;
			}
		};
		for(File root : roots) {
			new Thread(new FileCrawler(queue,filter,root)).start();
		}
		
		for(int i=0; i<N_CONSUMERS; i++) {
			new Thread(new Indexer(queue)).start();
		}
	}
	
	public static void main(String[] args) {
		File [] files = new File[1];
		files[0] = new File("/Users/zhenhua/Music/网易云音乐");
		startIndexing(files);
	}
}
