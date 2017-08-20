package net.jcip.examples.ch07;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

/**
 * 还可以这样利用Executor和Future来取消非interruptable的线程任务，
 * 记住了吗？
 * @author Jelex.xu
 * @date 2017年8月17日
 */
public abstract class SocketUsingTask<T> implements CancellableTask<T> {

	@GuardedBy("this") private Socket socket;
	
	public synchronized void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public synchronized void cancel() {
		if(socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
			}
		}
	}
	@Override
	public RunnableFuture<T> newTask() {
		return new FutureTask<T>(this) {

			@SuppressWarnings("finally")
			@Override
			public boolean cancel(boolean mayInterruptIfRunning) {
				try {
					SocketUsingTask.this.cancel();
				} finally {
					return super.cancel(mayInterruptIfRunning);
				}
			}
			
		};
	}
	
}

interface CancellableTask<T> extends Callable<T> {
	//2个新增的方法
	void cancel();
	
	RunnableFuture<T> newTask();
}

@ThreadSafe
class CancellingExecutor extends ThreadPoolExecutor {

	public CancellingExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}

	public CancellingExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
	}

	public CancellingExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
	}

	public CancellingExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
	}

	@Override
	protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
		if(callable instanceof CancellableTask) {
			return ((CancellableTask<T>) callable).newTask();
		}
		return super.newTaskFor(callable);
	}
	
	
	
}
