package net.jcip.examples.ch06;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RenderWithTimeBudget {

	//默认投放的广告
	private static final Ad DEFAULT_AD = new Ad();
	private static final long TIME_BUDGET = 1000;
	private static final ExecutorService exec = Executors.newCachedThreadPool();
	
	Page rendPageWithAd() throws InterruptedException {
		
		long endNanos = System.nanoTime() + TIME_BUDGET;
		Future<Ad> f = exec.submit(new FetchAdTask());
		//Render the page while waiting for the ad.
		Page page = rendPageBody();
		long timeLeft = endNanos - System.nanoTime();
		Ad ad;
		try {
			ad = f.get(timeLeft, TimeUnit.NANOSECONDS);
		} catch (ExecutionException e) {
			ad = DEFAULT_AD;
		} catch (TimeoutException e) {
			ad = DEFAULT_AD;
			f.cancel(true);
		}
		page.setAd(ad);
		return page;
	}
	
	Page rendPageBody() {
		return new Page();
	}

	/*
	 * 广告类
	 */
	static class Ad {
		
	}
	
	/*
	 * 页面类
	 */
	static class Page {
		public void setAd(Ad ad) {
		}
	}
	/*
	 * 抓取广告放到页面中的类
	 */
	static class FetchAdTask implements Callable<Ad> {
		@Override
		public Ad call() throws Exception {
			return new Ad();
		}
		
	}
}
