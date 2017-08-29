package net.jcip.examples.ch12;

/**
 * 测试puttaketest耗时功能辅助类
 * @author Jelex.xu
 * @date 2017年8月29日
 */
public class BarrierTimer implements Runnable {

	private boolean started;
	private long startTime, endTime;
	
	@Override
	public synchronized void run() {
		
		long t = System.nanoTime();
		if(!started) {
			started = true;
			startTime = t;
		} else {
			endTime = t;
		}
	}
	
	public synchronized void clear() {
		started = false;
	}
	
	public synchronized long getTime() {
		return endTime - startTime;
	}

}
