package net.jcip.examples.ch15;

import java.util.concurrent.atomic.AtomicReference;

import net.jcip.annotations.ThreadSafe;
/**
 * 使用AtomicReference来模拟LinkedQueue，
 * 它和之前模拟ConcurrentStack不同的是，
 * 这个程序需要同时更新2个变量,head, tail，
 * 而ConcurrentStack只需要处理一个变量：top.
 * @author Jelex.xu
 * @date 2017年9月3日
 */
@ThreadSafe
public class LinkedQueue<E> {

	private static class Node<E> {
		final E item;
		final AtomicReference<Node<E>> next;
		
		public Node(E item, Node<E> next) {
			this.item = item;
			this.next = new AtomicReference<Node<E>>(next);
		}
	}
	//哨兵
	private final Node<E> dummy = new Node<E>(null, null);
	
	private final AtomicReference<Node<E>> head =
			new AtomicReference<Node<E>>(dummy);
	
	private final AtomicReference<Node<E>> tail =
			new AtomicReference<Node<E>>(dummy);
	
	public boolean put(E item) {
		
		Node<E> newNode = new Node<E>(item, null);
		while(true) {
			Node<E> curTail = tail.get();
			Node<E> tailNext = curTail.next.get();
			if(curTail == tail.get()) {
				if(tailNext != null) {
					//Queue is in intermediate state,advance tail first.
					tail.compareAndSet(curTail, tailNext);
				}
				else {
					//在不活动状态，插入新节点到最后成功了！把当前最后一个节点
					//的指针指向了新插入的节点
					if(curTail.next.compareAndSet(null, newNode)) {
						//更新tail节点的指针指向该新插入的节点
						//不用管这一行操作的结果，因为如果不成功，那是因为
						//上面别的线程执行了第37行代码，它替当前线程
						//把这个活干了！
						tail.compareAndSet(curTail, newNode);
						return true;
					}
				}
			}
		}
	}
}
