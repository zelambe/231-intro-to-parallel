/*******************************************************************************
 * Copyright (C) 2016-2017 Dennis Cosgrove, Ben Choi
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
package util.lab.collection;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.wustl.cse231s.NotYetImplementedException;
import net.jcip.annotations.NotThreadSafe;

/**
 * @author Zahra Lambe
 * @author Ben Choi (benjaminchoi@wustl.edu)
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
@NotThreadSafe
/* package-private */ class LinkedNodesIterator<E> implements Iterator<E> {
	private LinkedNode<E> previous;
	private LinkedNode<E> current;
	private LinkedNodesCollection<E> collection;

	public LinkedNodesIterator(LinkedNodesCollection<E> collection) {
		current= collection.getHeadNode();
		previous= null;
		this.collection= collection;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNext() {
		if(current.getNext()!=null) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public E next() throws NoSuchElementException{
		if(hasNext()==true) {
			previous = current;
			current = current.getNext();
			return current.getValue();
		}
		else {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove() throws UnsupportedOperationException, IllegalStateException {
		//if it tries to remove the sentinnel, throw unsupported operation exception?
		//if remove has already been called, throw illegal state exception?
		//otherwise, remove the current node and point the previous node to the next node
		if(collection.size()>0) {
			if(current==collection.getHeadNode()) {
				throw new IllegalStateException();
			}
			else {
				if(previous==current) {
					throw new IllegalStateException();
				}
				else {
					previous.setNext(current.getNext());
					current = previous;
					collection.decrementSize();
				}
			}
		}
		else {
			throw new IllegalStateException();
		}
		
		
	}
}
