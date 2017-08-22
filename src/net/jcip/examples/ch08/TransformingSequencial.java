package net.jcip.examples.ch08;

import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
/**
 * 串行优化成并行计算
 * @author Jelex.xu
 * @date 2017年8月22日
 */
public abstract class TransformingSequencial {

	void processSequencially(List<Element> elements) {
		for(Element e : elements) {
			process(e);
		}
	}
	
	void processInParallel(Executor exec, List<Element> elements) {
		for(final Element e : elements) {
			exec.execute(new Runnable() {

				@Override
				public void run() {
					process(e);
				}
			});
		}
	}
	
	public abstract void process(Element e);
	
	interface Element {
		
	}
	
	/*
	 * 还可以这样搞！！！我指的是方法声明哦！
	 */
	public <T> void sequentialRecursive(List<Node<T>> nodes, Collection<T> results) {
		for(Node<T> n : nodes) {
			results.add(n.compute());
			sequentialRecursive(n.getChildren(), results);
		}
	}
	
	public <T> void paralledRecursive(final Executor exec, List<Node<T>> nodes, final Collection<T> results) {
		for(final Node<T> n : nodes) {
			exec.execute(new Runnable() {

				@Override
				public void run() {
					results.add(n.compute());
				}
			});
			paralledRecursive(exec, n.getChildren(), results);
		}
	}
	/*
	 * 获取并行计算的递归计算结果
	 */
	public <T> Collection<T> getParalledResults(List<Node<T>> nodes) throws InterruptedException {
		ExecutorService exec = Executors.newCachedThreadPool();
		Queue<T> resultQueue = new ConcurrentLinkedQueue<T>();
		
		paralledRecursive(exec, nodes, resultQueue);
		
		exec.shutdown();
		exec.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
		
		return resultQueue;
	}
	
	interface Node<T> {
		T compute();
		List<Node<T>> getChildren();
	}
}
