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

import static edu.wustl.cse231s.v5.V5.launchApp;

import java.math.BigInteger;

import edu.wustl.cse231s.asymptoticanalysis.OrderOfGrowth;
import fibonacci.core.FibonacciCalculator;
import fibonacci.fun.MemoizationParallelFibonacciCalculator;
import fibonacci.studio.DynamicIterativeSequentialFibonacciCalculator;
import fibonacci.studio.LinearRecurrenceSequentialFibonacciCalculator;
import fibonacci.studio.MemoizationSequentialFibonacciCalculator;
import fibonacci.studio.RecurrenceRelationParallelFibonacciCalculator;
import fibonacci.studio.RecurrenceRelationSequentialFibonacciCalculator;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class FibonacciIterations {
	public static void main(String[] args) {
		FibonacciCalculator[] fibonacciCalculators = { new RecurrenceRelationSequentialFibonacciCalculator(),
				new RecurrenceRelationParallelFibonacciCalculator(), new MemoizationSequentialFibonacciCalculator(),
				new MemoizationParallelFibonacciCalculator(), new DynamicIterativeSequentialFibonacciCalculator(),
				new LinearRecurrenceSequentialFibonacciCalculator() };
		FibonacciCalculator fibonacciCalculator = fibonacciCalculators[0];

		int max = fibonacciCalculator.getOrderOfGrowth() == OrderOfGrowth.EXPONENTIAL ? 40 : 100;
		System.out.println(fibonacciCalculator);
		launchApp(() -> {
			for (int n = 0; n < max; n++) {
				BigInteger value = fibonacciCalculator.fibonacci(n);
				System.out.println(n + " " + value);
			}
		});
	}
}
