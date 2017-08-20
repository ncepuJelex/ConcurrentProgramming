package net.jcip.examples.ch07;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import net.jcip.annotations.GuardedBy;
/**
 * 注意到，抽象类，每个子类处理页面抓取的方式不同，所以它
 * 的processPage(URL url)是抽象类型的方法
 * @author Jelex.xu
 * @date 2017年8月20日
 */
public abstract class WebCrawler {

	private volatile TrackingExecutor exec;
	
	@GuardedBy("this") private final Set<URL> urlsToCrawl = new HashSet<URL>();
	//已经访问过的URL列表
	private final ConcurrentHashMap<URL, Boolean> seen = new ConcurrentHashMap<>();
	private static final long TIMEOUT = 500;
	private static final TimeUnit UNIT = TimeUnit.MILLISECONDS;
	
	public WebCrawler(URL startUrl) {
		//把需要抓取的url地址先放入到任务列表中
		urlsToCrawl.add(startUrl);
	}
	/*
	 * 初始化执行池,遍历url任务列表，提交任务，
	 * 清空任务列表
	 */
	public synchronized void start() {
		exec = new TrackingExecutor(Executors.newCachedThreadPool());
		for(URL u : urlsToCrawl) {
			submitCrawlTask(u);
		}
		urlsToCrawl.clear();
	}
	
	public synchronized void stop() throws InterruptedException {
		try {
			saveUncrawled(exec.shutdownNow());
			//如果exec terminated的了，那就去保存没执行完的任务
			if (exec.awaitTermination(TIMEOUT, UNIT)) {
				saveUncrawled(exec.getCancelledTasks());
			} 
		} finally {
			//释放资源！因为是volatile 修饰的exec，所以
			//很多用户都可以用。用完之后自然就得关闭了。
			//不过，初始化exec是单线程有序访问的，因为初始化exec的方法
			//是synchronized修饰
			exec = null;
		}
		
	}
	/*
	 * 保存未执行完成的任务
	 */
	private void saveUncrawled(List<Runnable> uncrawled) {
		for(Runnable task : uncrawled) {
			urlsToCrawl.add(((CrawlTask)task).getPage());
		}
	}
	//提交任务，就是让执行池去执行任务，
	//所以把url作为参数封装到一个 任务模型中，
	//然后扔到执行池中执行
	private void submitCrawlTask(URL u) {
		exec.submit(new CrawlTask(u));
	}
	/*
	 * 让具体每个子类是实现它自己的抓取业务流程
	 */
	protected abstract List<URL> processPage(URL url);
	
	/**
	 * 抓取任务内部类，实现Runnable接口
	 * @author Jelex.xu
	 * @date 2017年8月20日
	 */
	private class CrawlTask implements Runnable {
	
		private final URL url;
		//这是何意？
		private int count;
		
		CrawlTask(URL url) {
			this.url = url;
		}

		@Override
		public void run() {
			//看这里哦，对一个url操作处理，搞出了好多URL
			for(URL link : processPage(url)) {
				//如果当前执行任务线程被打断了，直接返回
				if(Thread.currentThread().isInterrupted()) {
					return;
				}
				//提交任务
				submitCrawlTask(link);
			}
		}
		/*
		 *以便未完成执行的url，放到待下次执行列表中 
		 */
		public URL getPage() {
			return url;
		}
		//在每个子类中会使用到
		boolean alreadyCrawled() {
			return seen.putIfAbsent(url, true) != null;
		}
		//子类中使用
		void markUncrawled() {
			seen.remove(url);
			System.out.printf("marking %s uncrawled %n", url);
		}
		
	}
	
}
