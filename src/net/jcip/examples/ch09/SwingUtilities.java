package net.jcip.examples.ch09;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

/**
 * 模拟swing 的event thread,作者说如果java.util.concurent包出现
 * 后再有swing的话，那么它的event thread dispatcher 就会这样实现！
 * 没想到传入的ThreadFactory还能这么用！
 * @author Jelex.xu
 * @date 2017年8月23日
 */
public class SwingUtilities {

	private static final ExecutorService exec = Executors.newSingleThreadExecutor(new SwingThreadFactory());
	
	private static volatile Thread swingThread;
	
	private static class SwingThreadFactory implements ThreadFactory {
		@Override
		public Thread newThread(Runnable r) {
			swingThread = new Thread(r);
			return swingThread;
		}
	}
	
	public static boolean isEventDispatchThread() {
		return swingThread == Thread.currentThread();
	}
	
	public static void invokeLater(Runnable task) {
		exec.execute(task);
	}
	
	public static void invokeAndWait(Runnable task) throws InvocationTargetException, InterruptedException {
		Future f = exec.submit(task);
		try {
			f.get();
		} catch (ExecutionException e) {
			throw new InvocationTargetException(e);
		}
	}
}
