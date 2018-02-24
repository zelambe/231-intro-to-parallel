/*******************************************************************************
 * Copyright (C) 2016-2017 Dennis Cosgrove
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package fibonacci.studio;

import static edu.wustl.cse231s.v5.V5.doWork;
import static edu.wustl.cse231s.v5.V5.future;

import java.math.BigInteger;
import java.util.concurrent.Future;

import edu.wustl.cse231s.NotYetImplementedException;
import edu.wustl.cse231s.asymptoticanalysis.OrderOfGrowth;
import fibonacci.core.FibonacciCalculator;

/**
 * @author Zahra Lambe
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class MemoizationSequentialFibonacciCalculator implements FibonacciCalculator {
	private BigInteger fibonacciMemo(BigInteger[] memos, int n) {
			if (n == 0) {
				return BigInteger.ZERO;
			}
			else if (n == 1) {
				return BigInteger.ONE;
	
			} else {
				if(memos[n]!=null) {
				}
				else {
					memos[n-1] = fibonacciMemo(memos,n-1);
					memos[n-2] = fibonacciMemo(memos,n-2);
					memos[n]= memos[n-1].add(memos[n-2]);
				}
				return memos[n];
			}
			
	}

	@Override
	public BigInteger fibonacci(int n) {
		BigInteger[] memos = new BigInteger[n + 1];
		return fibonacciMemo(memos, n);
	}

	@Override
	public OrderOfGrowth getOrderOfGrowth() {
		return OrderOfGrowth.LINEAR;
	}

	@Override
	public boolean isSusceptibleToStackOverflowError() {
		return true;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}
