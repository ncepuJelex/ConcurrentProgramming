package net.jcip.examples.ch08;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
/**
 * 串行查找puzzle方案
 * @author Jelex.xu
 * @date 2017年8月22日
 */
public class SequentialPuzzleSolver<P, M> {

	private final Puzzle<P, M> puzzle;
	private final Set<P> seen = new HashSet<P>();

	public SequentialPuzzleSolver(Puzzle<P, M> puzzle) {
		this.puzzle = puzzle;
	}
	/*
	 * 初始化。。。
	 */
	public List<M> solve() {
		P pos = puzzle.initialPosition();
		return search(new PuzzleNode<P, M>(pos, null, null));
	}

	/*
	 * 解决实际调用的方法
	 */
	private List<M> search(PuzzleNode<P, M> puzzleNode) {
		if(!seen.contains(puzzleNode.pos)) {
			seen.add(puzzleNode.pos);
			if(puzzle.isGoal(puzzleNode.pos)) {
				return puzzleNode.asMoveList();
			}
			for(M move : puzzle.legalMoves(puzzleNode.pos)) {
				P pos = puzzle.move(puzzleNode.pos, move);
				PuzzleNode<P, M> child = new PuzzleNode<P, M>(pos, move, puzzleNode);
				List<M> result = search(child);
				if(result != null) {
					return result;
				}
			}
		}
		return null;
	}
}
