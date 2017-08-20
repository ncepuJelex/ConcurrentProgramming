package net.jcip.examples.ch06;

import java.util.concurrent.Executor;

/**
 * 使用Executor模拟单个线程处理所有任务，就是这么easy!
 * @author zhenhua
 * @date 2017年8月16日
 */
public class WithinThreadExecutor implements Executor {

	@Override
	public void execute(Runnable command) {
		command.run();
	}

}
