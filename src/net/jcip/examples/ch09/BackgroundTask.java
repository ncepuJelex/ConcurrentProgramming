package net.jcip.examples.ch09;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
/**
 * Background task class supporting cancellation, completion 
 * notification and progress notification.
 * @author Jelex.xu
 * @date 2017年8月24日
 */
public abstract class BackgroundTask<V> implements Runnable, Future<V> {

	private final FutureTask<V> compute = new Computation();
	
	private class Computation extends FutureTask<V> {

		public Computation() {
			super(new Callable<V>() {
				@Override
				public V call() throws Exception {
					return BackgroundTask.this.compute();
				}
			});
		}

		@Override
		protected void done() {
			GUIExecutor.instance().execute(new Runnable() {
				@Override
				public void run() {
					V value = null;
					Throwable thrown = null;
					boolean cancelled = false;
					try {
						value = get(); //父类中的方法
					} catch (InterruptedException e) {
					} catch (ExecutionException e) {
						thrown = e.getCause();
					} catch(CancellationException e) {
						cancelled = true;
					} finally {
						onCompletion(value, thrown, cancelled);
					}
				}
			});
		}
	}
	
	/*
	 * 完成之前收尾工作
	 */
	protected void onCompletion(V value, Throwable thrown, boolean cancelled) {
	}
	
	protected void setProgress(final int current, final int max) {
		GUIExecutor.instance().execute(new Runnable() {
			@Override
			public void run() {
				onProgress(current, max);
			}
		});
	}
	
	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return compute.cancel(mayInterruptIfRunning);
	}

	@Override
	public boolean isCancelled() {
		return compute.isCancelled();
	}

	@Override
	public boolean isDone() {
		return compute.isDone();
	}

	@Override
	public V get() throws InterruptedException, ExecutionException {
		return compute.get();
	}

	@Override
	public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		return compute.get(timeout, unit);
	}

	@Override
	public void run() {
		compute.run();
	}

	protected void onProgress(int current, int max) {
	}
	protected abstract V compute();
}
