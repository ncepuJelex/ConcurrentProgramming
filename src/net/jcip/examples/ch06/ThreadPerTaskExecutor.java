package net.jcip.examples.ch06;

import java.util.concurrent.Executor;

/**
 * 使用Executor模拟每个线程处理每个任务，就是这么easy!
 * @author zhenhua
 * @date 2017年8月16日
 */
public class ThreadPerTaskExecutor implements Executor {

	@Override
	public void execute(Runnable command) {
		new Thread(command).start();
	}

}
