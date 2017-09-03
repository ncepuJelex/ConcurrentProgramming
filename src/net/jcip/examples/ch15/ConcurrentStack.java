package net.jcip.examples.ch15;

import java.util.concurrent.atomic.AtomicReference;

import net.jcip.annotations.ThreadSafe;

/**
 * 使用AtomicReference来实现Concurrent Stack,还可以
 * 这样搞！！！
 * @author Jelex.xu
 * @date 2017年9月3日
 */
@ThreadSafe
public class ConcurrentStack<E> {

	AtomicReference<Node<E>> top = new AtomicReference<Node<E>>();
	
	public void push(E item) {
		Node<E> newHead = new Node<E>(item);
		Node<E> oldHead;
		do{
			oldHead = top.get();
			newHead.next = oldHead;
		} while(!top.compareAndSet(oldHead, newHead));
	}
	
	public E pop() {
		Node<E> oldHead;
		Node<E> newHead;
		do{
			oldHead = top.get();
			if(oldHead == null) {
				return null;
			}
			newHead = oldHead.next;
		} while(!top.compareAndSet(oldHead, newHead));
		return oldHead.item;
	}
	
	private static class Node<E> {
		public final E item;
		public Node<E> next;
		
		public Node(E item) {
			this.item = item;
		}
		
		
	}
}
