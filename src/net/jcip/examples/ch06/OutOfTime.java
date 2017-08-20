package net.jcip.examples.ch06;

import java.util.Timer;
import java.util.TimerTask;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * 这个程序不会跑6s的，启动后直接拋出异常，这个程序
 * 把Timer黑得漂亮！还是使用ScheduleThreadPoolExecutor吧！
 * @author zhenhua
 * @date 2017年8月16日
 */
public class OutOfTime {

	public static void main(String[] args) throws InterruptedException {
		Timer timer = new Timer();
		timer.schedule(new ThrowTask(), 1);
		SECONDS.sleep(1);
		timer.schedule(new ThrowTask(), 1);
		SECONDS.sleep(5);
	}
	
	static class ThrowTask extends TimerTask {
		@Override
		public void run() {
			throw new RuntimeException();
		}
		
	}
}
