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
package fibonacci.fun;

import static edu.wustl.cse231s.v5.V5.launchApp;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import edu.wustl.cse231s.junit.JUnitUtils;
import fibonacci.core.FibonacciCalculator;
import fibonacci.core.FibonacciUtils;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
@RunWith(Parameterized.class)
public class OptionalFunFibonacciTest {
	private final FibonacciCalculator fibonacciCalculator;
	private final int n;

	public OptionalFunFibonacciTest(FibonacciCalculator fibonacciCalculator, int n) {
		this.fibonacciCalculator = fibonacciCalculator;
		this.n = n;
	}

	@Rule
	public TestRule timeout = JUnitUtils.createTimeoutRule();

	@Test
	public void test() {
		BigInteger expected = FibonacciUtils.fibonacci(n);
		launchApp(() -> {
			BigInteger value = this.fibonacciCalculator.fibonacci(n);
			Assert.assertEquals(expected, value);
		});
	}

	@Parameters(name = "{0} n={1}")
	public static Collection<Object[]> getConstructorArguments() {
		int n = 31;
		List<FibonacciCalculator> fibonacciCalculators = Arrays.asList(
				new DynamicRecursiveSequentialFibonacciCalculator(),
				new RecurrenceRelationParallelWithThresholdFibonacciCalculator(n - 3),
				new MemoizationParallelFibonacciCalculator(),
				new RoundPhiToTheNOverSqrt5SequentialFibonacciCalculator());

		List<Object[]> list = new LinkedList<>();
		for (FibonacciCalculator fibonacciCalculator : fibonacciCalculators) {
			list.add(new Object[] { fibonacciCalculator, n });
		}
		return list;
	}
}
