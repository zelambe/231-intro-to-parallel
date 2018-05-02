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
package kmer.lab.concurrentbuckethashmap;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.BiFunction;

import edu.wustl.cse231s.NotYetImplementedException;
import edu.wustl.cse231s.util.KeyMutableValuePair;
import net.jcip.annotations.ThreadSafe;

/**
 * @author Zahra Lambe
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
@ThreadSafe
/* package-private */ class ConcurrentBucketHashMap<K, V> implements ConcurrentMap<K, V> {
	private final List<Entry<K, V>>[] buckets;
	private final ReadWriteLock[] locks;

	@SuppressWarnings("unchecked")
	public ConcurrentBucketHashMap(int bucketCount) {
		buckets = new List[bucketCount];
		for (int i=0; i< buckets.length; i++) {
			buckets[i] = new LinkedList<Entry<K,V>>();
		}
		locks= new ReadWriteLock[bucketCount]; //???? just guessing
		
		throw new NotYetImplementedException();
	}

	private int getIndex(Object key) {
		int hashCode = key.hashCode();
		return Math.floorMod(hashCode, buckets.length);
	}

	private List<Entry<K, V>> getBucket(Object key) {
		int bucketIndex = getIndex(key);
		return buckets[bucketIndex];
	}

	private ReadWriteLock getLock(Object key) {
		return locks[getIndex(key)];
	}

	private static <K, V> Entry<K, V> getEntry(List<Entry<K, V>> bucket, Object key) { // what
		for(Entry<K,V> e : bucket) {
			if(e.getKey() == key) {
				return e;
			}else {
				return null;
			}
		}
		throw new NotYetImplementedException();

	}

	@Override
	public V get(Object key) { //not thread safe yet
		Collection<Entry<K,V>> bucket =getBucket(key);
		Iterator<Entry<K, V>> mapIterator = bucket.iterator();
		while (mapIterator.hasNext() == true) {
			KeyMutableValuePair<K, V> pair = (KeyMutableValuePair<K, V>) mapIterator.next();
			if (pair.getKey().equals(key)) {
				return pair.getValue();
			}
		}
		return null;
	}

	@Override
	public V put(K key, V value) { //not thread safe yet.
		Collection<Entry<K,V>> bucket =getBucket(key);
		Iterator<Entry<K, V>> mapIterator = bucket.iterator();
		while(mapIterator.hasNext()==true) {
			KeyMutableValuePair<K, V> pair = (KeyMutableValuePair<K, V>) mapIterator.next();
			if (pair.getKey().equals(key)) {
				V oldValue = pair.getValue();
				pair.setValue(value);
				return oldValue;
			}
		}
		bucket.add(new KeyMutableValuePair<>(key,value));
		return null;	
		}

	@Override
	public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) { //what am i computing lol?
//		 (key(k,v) ->{
//			 
//		});
		throw new NotYetImplementedException();
	}

	@Override
	public int size() {
		throw new RuntimeException("not required");
	}

	@Override
	public boolean isEmpty() {
		throw new RuntimeException("not required");
	}

	@Override
	public boolean containsKey(Object key) {
		throw new RuntimeException("not required");
	}

	@Override
	public boolean containsValue(Object value) {
		throw new RuntimeException("not required");
	}

	@Override
	public V putIfAbsent(K key, V value) {
		throw new RuntimeException("not required");
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		throw new RuntimeException("not required");
	}

	@Override
	public boolean replace(K key, V oldValue, V newValue) {
		throw new RuntimeException("not required");
	}

	@Override
	public V replace(K key, V value) {
		throw new RuntimeException("not required");
	}

	@Override
	public void clear() {
		throw new RuntimeException("not required");
	}

	@Override
	public boolean remove(Object key, Object value) {
		throw new RuntimeException("not required");
	}

	@Override
	public V remove(Object key) {
		throw new RuntimeException("not required");
	}

	@Override
	public Set<K> keySet() {
		throw new RuntimeException("not required");
	}

	@Override
	public Collection<V> values() {
		throw new RuntimeException("not required");
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		throw new RuntimeException("not required");
	}
}
