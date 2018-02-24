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

import static edu.wustl.cse231s.v5.V5.future;
import static edu.wustl.cse231s.v5.V5.doWork;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import edu.wustl.cse231s.NotYetImplementedException;
import edu.wustl.cse231s.asymptoticanalysis.OrderOfGrowth;
import fibonacci.core.FibonacciCalculator;

/**
 * @author Zahra Lambe
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class RecurrenceRelationParallelFibonacciCalculator implements FibonacciCalculator {
	@Override
	public BigInteger fibonacci(int n) throws InterruptedException, ExecutionException {
		doWork(1);
		if(n==0) {
			return BigInteger.ZERO;
		}
		if(n==1) {
			return BigInteger.ONE;
		}
		else {
			Future<BigInteger> fib1 = future (() ->{
				return fibonacci(n-1);
			});
			
			Future<BigInteger> fib2 = future (() ->{
				return fibonacci(n-2);
			});
			return fib1.get().add(fib2.get());
		}
	
	}

	@Override
	public OrderOfGrowth getOrderOfGrowth() {
		// WORK: O(Phi^n), CPL: O(n)
		return OrderOfGrowth.EXPONENTIAL;
	}

	@Override
	public boolean isSusceptibleToStackOverflowError() {
		return false;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}
