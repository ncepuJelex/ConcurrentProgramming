package net.jcip.examples.ch08;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
/**
 * 找到一种方案后，使用配置好的discard policy策略把
 * 之后的任务丢掉，因为只需要一个方案，而且已经找到了它！
 * 但是这个程序没有处理当没有一种方案时的处理，下面看它的子类终极版。
 * @author Jelex.xu
 * @date 2017年8月22日
 */
public class ConcurrentPuzzleSolver<P, M> {

	private final Puzzle<P, M> puzzle;
	private final ExecutorService exec;
	private final ConcurrentMap<P, Boolean> seen;
	protected final ValueLatch<PuzzleNode<P, M>> solution = new ValueLatch<PuzzleNode<P, M>>();
	
	public ConcurrentPuzzleSolver(Puzzle<P, M> puzzle) {
		this.puzzle = puzzle;
		this.exec = initThreadPool();
		this.seen = new ConcurrentHashMap<P, Boolean>();
		
		if(exec instanceof ThreadPoolExecutor) {
			ThreadPoolExecutor tpe = (ThreadPoolExecutor) exec;
			tpe.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
		}
	}

	private ExecutorService initThreadPool() {
		return Executors.newCachedThreadPool();
	}
	
	public List<M> solve() throws InterruptedException {
		try {
			P p = puzzle.initialPosition();
			exec.execute(newTask(p, null, null));
			//block until solution found.
			PuzzleNode<P, M> solutionNode = solution.getValue();
			return (solutionNode == null ? null : solutionNode.asMoveList());
		} finally {
			exec.shutdown();
		}
	}
	
	protected Runnable newTask(P p, M m, PuzzleNode<P, M> n) {
		return new SolveTask(p, m, n);
	}
	
	protected class SolveTask extends PuzzleNode<P, M> implements Runnable {

		public SolveTask(P pos, M move, PuzzleNode<P, M> prev) {
			super(pos, move, prev);
		}

		@Override
		public void run() {

			if(solution.isSet() || seen.putIfAbsent(pos, true) != null) {
				return; //already found the answer or seen this position.
			}
			if(puzzle.isGoal(pos)) {
				solution.setValue(this);
			} else {
				for(M m : puzzle.legalMoves(pos)) {
					exec.execute(newTask(puzzle.move(pos, m), m, this));
				}
			}
		}
		
	}
}
