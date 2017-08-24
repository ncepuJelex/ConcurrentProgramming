package net.jcip.examples.ch09;

import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.TimeUnit;
/**
 * GUI executor，利用之前模拟的Event Dispatch Thread Executor.
 * @author Jelex.xu
 * @date 2017年8月23日
 */
public class GUIExecutor extends AbstractExecutorService {

	private static final GUIExecutor instance = new GUIExecutor();
	
	private GUIExecutor() {}
	
	public static GUIExecutor instance() {
		return instance;
	}
	
	@Override
	public void shutdown() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Runnable> shutdownNow() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isShutdown() {
		return false;
	}

	@Override
	public boolean isTerminated() {
		return false;
	}

	@Override
	public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
		throw new UnsupportedOperationException();
	}

	/*
	 * 关键看这里(non-Javadoc)
	 * @see java.util.concurrent.Executor#execute(java.lang.Runnable)
	 */
	@Override
	public void execute(Runnable command) {
		if(SwingUtilities.isEventDispatchThread()) {
			command.run();
		} else {
			SwingUtilities.invokeLater(command);
		}
	}

}
