package net.jcip.examples.ch07;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 使用POISON来达到取消任务的目的，
 * 在任务的最后加个 有毒的一个标识，当消费者看到
 * 毒物时，就知道应该退出了
 * @author Jelex.xu
 * @date 2017年8月19日
 */
public class IndexingService {

	private static final int CAPACITY = 1000;
	private static final File POISON = new File("");
	private final CrawlerThread producer = new CrawlerThread();
	private final IndexerThread consumer = new IndexerThread();
	private final FileFilter fileFilter;
	private final File root;
	private final BlockingQueue<File> queue;
	
	public IndexingService(FileFilter fileFilter, File root) {
		this.root = root;
		queue = new LinkedBlockingQueue<File>(CAPACITY);
		this.fileFilter = new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				return pathname.isDirectory() || fileFilter.accept(pathname);
			}
			
		};
	}

	private boolean alreadyIndexed(File f) {
		return false;
	}
	
	
	public void start() {
		producer.start();
		consumer.start();
	}
	
	public void stop() {
		producer.interrupt();
	}
	
	public void awaitTermination() throws InterruptedException {
		consumer.join();
	}
	
	class CrawlerThread extends Thread {

		@Override
		public void run() {
			try {
				crawl(root);
			} catch (InterruptedException e) {
			} finally {
				while(true) {
					try {
						queue.put(POISON);
						break;
					} catch (InterruptedException e) {
						/*retry*/
					}
				}
			}
		}

		private void crawl(File root) throws InterruptedException {
			File[] entries = root.listFiles(fileFilter);
			if(entries != null) {
				for(File entry : entries) {
					if(entry.isDirectory()) {
						crawl(entry);
					}
					else if(!alreadyIndexed(entry)) {
						queue.put(entry);
					}
				}
			}
		}
	}
	
	class IndexerThread extends Thread {

		@Override
		public void run() {
			try {
				while(true) {
					File file = queue.take();
					if(file == POISON) {
						break;
					} else {
						indexFile(file);
					}
				}
			} catch (InterruptedException e) {
			}
		}

		private void indexFile(File file) {
			/*...*/
			System.out.println(file.getAbsolutePath());
		}
		
	}
	
	public static void main(String[] args) {
		File file = new File("/Users/zhenhua/Music/网易云音乐");
		
		FileFilter filter = new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return true;
			}
		};
		
		new IndexingService(filter, file).start();
	}
}
