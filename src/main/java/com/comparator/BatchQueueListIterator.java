package com.comparator;

import java.util.Iterator;

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
        return batchQueueList.next();
    }
}
