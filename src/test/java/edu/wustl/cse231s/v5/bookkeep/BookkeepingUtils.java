package edu.wustl.cse231s.v5.bookkeep;

import static edu.wustl.cse231s.v5.V5.launchApp;

import edu.wustl.cse231s.v5.api.CheckedRunnable;
import edu.wustl.cse231s.v5.impl.BookkeepingV5Impl;
import edu.wustl.cse231s.v5.impl.executor.BookkeepingExecutorXV5Impl;

public class BookkeepingUtils {
	public static BookkeepingV5Impl bookkeep(CheckedRunnable body) {
		BookkeepingExecutorXV5Impl bookkeeping = new BookkeepingExecutorXV5Impl();
		launchApp(bookkeeping, body);
		return bookkeeping;
	}
}
