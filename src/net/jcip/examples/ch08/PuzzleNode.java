package net.jcip.examples.ch08;

import java.util.LinkedList;
import java.util.List;

import net.jcip.annotations.Immutable;

/**
 * 通过一些移动达到当前Position状态，
 * 它代表移动中的某个状态，初始化时，move和prev都为null,
 * 然后从这个状态开始去寻找方案。
 * @author Jelex.xu
 * @date 2017年8月22日
 */
@Immutable
public class PuzzleNode<P, M> {

	final P pos;
	final M move;
	final PuzzleNode<P, M> prev;

	public PuzzleNode(P pos, M move, PuzzleNode<P, M> prev) {
		this.pos = pos;
		this.move = move;
		this.prev = prev;
	}
	/*
	 * 追本溯源
	 */
	List<M> asMoveList() {
		List<M> solution = new LinkedList<M>();
		for(PuzzleNode<P, M> n = this; n.move != null; n = n.prev) {
			solution.add(0, n.move);
		}
		return solution;
	}
	
}
