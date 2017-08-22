package net.jcip.examples.ch08;

import java.util.Set;

/**
 * Puzzle总的功能布局
 * @author Jelex.xu
 * @date 2017年8月22日
 */
public interface Puzzle <P, M>{

	P initialPosition();
	
	boolean isGoal(P position);
	
	Set<M> legalMoves(P position);
	
	P move(P position, M move);
}
