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
package tnx.lab.executor;

import static edu.wustl.cse231s.v5.V5.async;
import static edu.wustl.cse231s.v5.V5.finish;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import count.assignment.NucleobaseCounting;
import edu.wustl.cse231s.IntendedForStaticAccessOnlyError;
import edu.wustl.cse231s.NotYetImplementedException;
import edu.wustl.cse231s.bioinformatics.Nucleobase;
import midpoint.assignment.MidpointUtils;
import slice.core.Slice;
import slice.studio.Slices;
import tnx.lab.thread.SimpleThreadFactory;

/**
 * A parallel nucleobase counter that uses Java's {@link ExecutorService}
 * interface to count different sections of the chromosome in parallel.
 * 
 * @author Zahra Lambe
 * @author Finn Voichick
 */
public class XNucleobaseCounting {

	/**
	 * This class is noninstantiable. Do not modify or call this constructor.
	 */
	private XNucleobaseCounting() {
		throw new IntendedForStaticAccessOnlyError();
	}

	/**
	 * Should count all of the instances of a specific nucleobase in parallel. The
	 * chromosome should be split into two halves, and the "lower" half should be
	 * counted at the same time (asynchronously) as the "upper" half.
	 * 
	 * @param executor
	 *            an {@code ExecutorService} that you should use to submit tasks
	 * @param chromosome
	 *            the chromosome to examine, represented as an array of bytes
	 * @param nucleobase
	 *            the nucleobase to look for in the chromosome
	 * @return the total number of times that the given nucleobase occurs in the
	 *         given chromosome
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public static int countLowerUpperSplit(ExecutorService executor, byte[] chromosome, Nucleobase nucleobase)
			throws InterruptedException, ExecutionException {
		int midpoint = MidpointUtils.calculateMidpoint(0, chromosome.length);

		Future<Integer> lowerSum = executor.submit(() -> { // lower half
			return NucleobaseCounting.countRangeSequential(chromosome, nucleobase, 0, midpoint);
		});

		int upperSum = NucleobaseCounting.countRangeSequential(chromosome, nucleobase, midpoint, chromosome.length); // upper
																														// half
		return lowerSum.get() + upperSum; // sum

	}

	/**
	 * Should asynchronously count all of the instances of a specific nucleobase,
	 * creating the given number of tasks. For example, if numTasks is 8, the
	 * chromosome should be divided into 8 pieces, and each of these pieces should
	 * be counted in a separate task.
	 * 
	 * NOTE: You are required to use the
	 * {@link ExecutorService#invokeAll(Collection)} method for this part of the
	 * assignment.
	 * 
	 * @param executor
	 *            an {@code ExecutorService} that you should use to invoke tasks
	 * @param chromosome
	 *            the chromosome to examine, represented as an array of bytes
	 * @param nucleobase
	 *            the nucleobase to look for in the chromosome
	 * @param numTasks
	 *            the number of tasks to create
	 * @return the total number of times that the given nucleobase occurs in the
	 *         given chromosome
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public static int countNWaySplit(ExecutorService executor, byte[] chromosome, Nucleobase nucleobase, int numTasks)
			throws InterruptedException, ExecutionException {

		int totalSum = 0;

		List<Callable<Integer>> tasks = new ArrayList<Callable<Integer>>(numTasks); // list for tasks
		List<Slice<byte[]>> sliceList = new LinkedList<Slice<byte[]>>(); // list for slices of chromosomes
		sliceList = Slices.createNSlices(chromosome, numTasks); // put slices in the list
		for (Slice<byte[]> s : sliceList) { // for every slice, make a new task
			tasks.add(() -> {
				return NucleobaseCounting.countRangeSequential(chromosome, nucleobase, s.getMinInclusive(),
						s.getMaxExclusive());
			});
		}

		List<Future<Integer>> futures = executor.invokeAll(tasks); // start all of the tasks

		for (Future<Integer> future : futures) { // add up the total sum
			totalSum += future.get();
		}

		return totalSum;
	}

	/**
	 * Should use a divide-and-conquer approach to count all of the instances of a
	 * specific nucleobase.
	 * 
	 * @param executor
	 *            an {@code ExecutorService} that you should use to submit tasks
	 * @param chromosome
	 *            the chromosome to examine, represented as an array of bytes
	 * @param nucleobase
	 *            the nucleobase to look for in the chromosome
	 * @param threshold
	 *            the range length below which processing should be sequential to
	 *            reduce overhead
	 * @return the total number of times that the given nucleobase occurs in the
	 *         given chromosome
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public static int countDivideAndConquer(ExecutorService executor, byte[] chromosome, Nucleobase nucleobase,
			int threshold) throws InterruptedException, ExecutionException {
		return countDivideAndConquerKernel(executor, chromosome, nucleobase, 0, chromosome.length, threshold);
	}

	/**
	 * Should recursively use a divide-and-conquer approach to count all of the
	 * instances of a specific nucleobase in the given range. If the length is less
	 * than or equal to the threshold, it is not worth parallelizing, and this
	 * method should count sequentially using
	 * {@link #countRangeSequential(byte[], Nucleobase, int, int)}. Otherwise, it
	 * should recursively create two tasks for the lower and upper halves of this
	 * range.
	 * 
	 * @param executor
	 *            an {@code ExecutorService} that you should use to submit tasks
	 * @param chromosome
	 *            the chromosome to examine, represented as an array of bytes
	 * @param nucleobase
	 *            the nucleobase to look for in the chromosome
	 * @param min
	 *            the lowest array index in the range to search, inclusive
	 * @param maxExclusive
	 *            the highest array index in the range to search, exclusive
	 * @param threshold
	 *            the range length below which processing should be sequential to
	 *            reduce overhead
	 * @return the total number of times that the given nucleobase occurs in the
	 *         given chromosome within the given range
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	private static int countDivideAndConquerKernel(ExecutorService executor, byte[] chromosome, Nucleobase nucleobase,
			int min, int maxExclusive, int threshold) throws InterruptedException, ExecutionException {

		int length = maxExclusive - min;
		int midpoint = MidpointUtils.calculateMidpoint(min, maxExclusive);
		int[] subSums = { 0, 0 };

		if (length > threshold) {
			Future<Integer> lowerHalfFuture = executor.submit(() -> { //lower half
				return countDivideAndConquerKernel(executor, chromosome, nucleobase, min, midpoint, threshold);
			});
			subSums[1] = countDivideAndConquerKernel(executor, chromosome, nucleobase, midpoint, maxExclusive, //upperhalf
					threshold);
			subSums[0] = lowerHalfFuture.get();
			return subSums[0] + subSums[1];

		} else {
			return NucleobaseCounting.countRangeSequential(chromosome, nucleobase, min, maxExclusive); 
		}
	}
}
