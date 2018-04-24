/*******************************************************************************
 * Copyright (C) 2016-2018 Dennis Cosgrove
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

package sudoku.lab;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.SortedSet;
import java.util.TreeSet;

import edu.wustl.cse231s.NotYetImplementedException;
import edu.wustl.cse231s.util.KeyValuePair;
import sudoku.core.ConstraintPropagator;
import sudoku.core.Square;
import sudoku.core.io.PuzzlesResourceUtils;

/**
 * @author Zahra Lambe
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class PeerRemovingConstraintPropagator implements ConstraintPropagator {
	@Override
	public Map<Square, SortedSet<Integer>> createOptionSetsFromGivens(String givens) {
		int[][] values = PuzzlesResourceUtils.parseGivens(givens);
		
		EnumMap<Square, SortedSet<Integer>> map = new EnumMap(Square.class); 
		
		for (int i = 0; i < 9; i++) { // rows
			for (int j = 0; j < 9; j++) { // columns
				Square square = Square.valueOf(i, j);
				map.put(square, allOptions());
			}
		}
		
		for (int k=0; k<9; k++) {
			for (int m =0; m<9; m++) {
				if(values[k][m]!= 0) {
					associateValueWithSquareAndRemoveFromPeers(map, Square.valueOf(k,m), values[k][m]);
				}
			}
		}
		return map;
}

	@Override
	public Map<Square, SortedSet<Integer>> createNextOptionSets(Map<Square, SortedSet<Integer>> otherOptionSets,
			Square square, int value) {
		Map<Square,SortedSet<Integer>> map = deepCopyOf(otherOptionSets);
		associateValueWithSquareAndRemoveFromPeers(map,square,value);
		return map;
	}

	private static void associateValueWithSquareAndRemoveFromPeers(Map<Square, SortedSet<Integer>> resultOptionSets,
			Square square, int value) {
		
		resultOptionSets.replace(square, resultOptionSets.get(square), singleOption(value)); //associate value
		
		for (Square s: square.getPeers()) { // iterate through the peers
			if (resultOptionSets.get(s).remove(value) && resultOptionSets.get(s).size()==1) {
				associateValueWithSquareAndRemoveFromPeers(resultOptionSets,s,resultOptionSets.get(s).first());
			}
			
		}
		
	}

	private static SortedSet<Integer> singleOption(int value) {
		return new TreeSet<>(Arrays.asList(value));
	}

	private static SortedSet<Integer> allOptions() {
		return new TreeSet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
	}

	private static SortedSet<Integer> copyOf(SortedSet<Integer> other) {
		return new TreeSet<>(other);
	}

	private static Map<Square, SortedSet<Integer>> deepCopyOf(Map<Square, SortedSet<Integer>> other) {
		Map<Square, SortedSet<Integer>> result = new EnumMap<>(Square.class);
		for (Entry<Square, SortedSet<Integer>> entry : other.entrySet()) {
			result.put(entry.getKey(), copyOf(entry.getValue()));
		}
		return result;
	}
}
