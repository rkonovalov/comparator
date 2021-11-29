package com.comparator;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class BatchQueueListIterator<T> implements Iterator<T> {
    private BatchQueueList<T> batchQueueList;

    public BatchQueueListIterator(BatchQueueList<T> batchQueueList) {
        this.batchQueueList = batchQueueList;
    }

    @Override
    public boolean hasNext() {
        return batchQueueList.hasNext();
    }

    @Override
    public T next() {
        if(hasNext()) {
            return batchQueueList.next();
        } else
            throw new NoSuchElementException();
    }
}
