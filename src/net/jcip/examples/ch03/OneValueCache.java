package net.jcip.examples.ch03;

import java.math.BigInteger;
import java.util.Arrays;

import net.jcip.annotations.Immutable;

/**
 * 2个final变量，一旦被赋值，就不再变化了，安全！
 * 但是这依赖了copyOf（...）的帮助哦！不然就不是immutable了。
 * @author zhenhua
 * @date 2017年8月14日
 */
@Immutable
public class OneValueCache {

	private final BigInteger lastNumber;
	private final BigInteger [] lastFactors;
	
	public OneValueCache(BigInteger i, BigInteger [] factors) {
		lastNumber = i;
		lastFactors = Arrays.copyOf(factors,factors.length);
	}
	
	public BigInteger [] getFactors(BigInteger i) {
		if(lastNumber == null || !lastNumber.equals(i)) {
			return null;
		}
		else {
			return Arrays.copyOf(lastFactors, lastFactors.length);
		}
	}
	
}
