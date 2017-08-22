package net.jcip.examples.ch08;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 当没有方案时，返回null.
 * 通过一个计数器实现。
 * @author Jelex.xu
 * @date 2017年8月22日
 */
public class PuzzleSovle<P, M> extends ConcurrentPuzzleSolver<P, M>{

	public PuzzleSovle(Puzzle<P, M> puzzle) {
		super(puzzle);
	}
	
	private final AtomicInteger taskCount = new AtomicInteger();

	@Override
	protected Runnable newTask(P p, M m, PuzzleNode<P, M> n) {
		return new CountingSolveTask(p, m, n);
	}
	
	class CountingSolveTask extends SolveTask {

		public CountingSolveTask(P pos, M move, PuzzleNode<P, M> prev) {
			super(pos, move, prev);
			taskCount.incrementAndGet();
		}

		@Override
		public void run() {
			try {
				super.run();
			} finally {
				if(taskCount.decrementAndGet() == 0) {
					solution.setValue(null);
				}
			}
		}
		
		
	}
	

}
