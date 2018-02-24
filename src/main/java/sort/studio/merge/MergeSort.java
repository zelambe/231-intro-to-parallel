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
package sort.studio.merge;

import static edu.wustl.cse231s.v5.V5.async;
import static edu.wustl.cse231s.v5.V5.finish;

import java.util.concurrent.ExecutionException;

import edu.wustl.cse231s.NotYetImplementedException;
import midpoint.assignment.MidpointUtils;
import sort.core.merge.Combiner;

/**
 * @author Zahra Lambe
 * @author Aaron Handleman
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class MergeSort {
	/**
	 * @param data,
	 *            the array to sort
	 * @param lowInclusive,
	 *            the lower bound of the range to sort (inclusive)
	 * @param highExclusive,
	 *            the upper bound of the range to sort (exclusive)
	 * @param combiner
	 *            used to merge the two sub-problem solutions into one
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	private static void sequentialMergeSortKernel(int[] data, int lowInclusive, int highExclusive, Combiner combiner)
			throws InterruptedException, ExecutionException {
		int midpoint = MidpointUtils.calculateMidpoint(lowInclusive, highExclusive);
		if (highExclusive - lowInclusive != 1) {
			sequentialMergeSortKernel(data, lowInclusive, midpoint, combiner);
			sequentialMergeSortKernel(data, midpoint, highExclusive, combiner);
			// merging
			// int[] temp = new int[data.length];
			//
			// int left = lowInclusive;
			// int right = midpoint;
			// int index = lowInclusive;
			//
			// while (left < midpoint && right < highExclusive) {
			// if (data[left] <= data[right]) {
			// temp[index]= data[left];
			// }
			// else {
			// temp[index]= data[right];
			// }
			// index++;
			// }
			//
			// while(left<midpoint) {
			// temp[index++]= data[left++];
			// }
			//
			// while(right<highExclusive) {
			// temp[index++] = data[right++];
			// }
			// System.arraycopy(temp, lowInclusive, data, lowInclusive, highExclusive-1);
		}
		combiner.combineRange(data, lowInclusive, midpoint, highExclusive);
	}

	/**
	 * @param data,
	 *            the array to sort
	 * @param combiner
	 *            used to merge the two sub-problem solutions into one
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public static void sequentialMergeSort(int[] data, Combiner combiner)
			throws InterruptedException, ExecutionException {
		sequentialMergeSortKernel(data, 0, data.length, combiner);

	}

	/**
	 * Recursively and concurrently sorts the given array by breaking it down into
	 * arrays of size one, comparing the values, and merging them by order
	 * 
	 * @param data,
	 *            the array to sort
	 * @param lowInclusive,
	 *            the lower bound of the range to sort (inclusive)
	 * @param highExclusive,
	 *            the upper bound of the range to sort (exclusive)
	 * @param threshold,
	 *            the range length from lowInclusive to highExclusive below which to
	 *            transition from parallel divide and conquer to sequential divide
	 *            and conquer.
	 * @param combiner
	 *            used to merge the two sub-problem solutions into one
	 * @throws InterruptedException,
	 *             ExecutionException
	 */
	private static void parallelMergeSortKernel(int[] data, int lowInclusive, int highExclusive, int threshold,
			Combiner combiner) throws InterruptedException, ExecutionException {
		int midpoint = MidpointUtils.calculateMidpoint(lowInclusive, highExclusive);
		if (highExclusive - lowInclusive > threshold) {
			finish (() ->{
				async(() -> {
					sequentialMergeSortKernel(data, lowInclusive, midpoint, combiner);
				});
				async(() -> {
					sequentialMergeSortKernel(data, midpoint, highExclusive, combiner);
				});
			});
		}
		combiner.combineRange(data, lowInclusive, midpoint, highExclusive);
	}

	/**
	 * @param data,
	 *            the array to sort
	 * @param threshold,
	 *            the range length below which to transition from parallel divide
	 *            and conquer to sequential divide and conquer.
	 * @param combiner
	 *            used to merge the two sub-problem solutions into one
	 * @throws InterruptedException,
	 *             ExecutionException
	 */
	public static void parallelMergeSort(int[] data, int threshold, Combiner combiner)
			throws InterruptedException, ExecutionException {
		parallelMergeSortKernel(data,0,data.length,threshold,combiner);
	}
}
