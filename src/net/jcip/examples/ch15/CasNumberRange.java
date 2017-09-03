package net.jcip.examples.ch15;

import java.util.concurrent.atomic.AtomicReference;

import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;

/**
 * 使用CAS来组建一对变量的原子性操作
 * 
 * @author Jelex.xu
 * @date 2017年9月3日
 */
@ThreadSafe
public class CasNumberRange {

	@Immutable
	private static class IntPair {
		// invariant: lower <= upper
		final int lower;
		final int upper;

		public IntPair(int lower, int upper) {
			this.lower = lower;
			this.upper = upper;
		}
	}

	private final AtomicReference<IntPair> values = new AtomicReference<IntPair>(new IntPair(0, 0));

	public int getLower() {
		return values.get().lower;
	}

	public int getUpper() {
		return values.get().upper;
	}

	public void setLower(int i) {
		while (true) {
			IntPair oldv = values.get();
			if (i > oldv.upper) {
				throw new IllegalArgumentException("Can't lower value to " + i + ", which is bigger than upper value.");
			}
			IntPair newv = new IntPair(i, oldv.upper);
			if (values.compareAndSet(oldv, newv)) {
				return;
			}
		}
	}

	public void setUpper(int i) {
		while (true) {
			IntPair oldv = values.get();
			if (i < oldv.lower) {
				throw new IllegalArgumentException(
						"hey,the upper value " + i + " you set is smaller than the lower value.");
			}
			IntPair newv = new IntPair(oldv.lower, i);
			if (values.compareAndSet(oldv, newv)) {
				return;
			}
		}
	}
}
