package com.comparator;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.ReentrantLock;

public class BatchQueueListItem<T> implements BatchQueueItem<T> {
    private List<T> items = new ArrayList<>();
    private int itemIndex;
    private ReentrantLock lock = new ReentrantLock();

    public BatchQueueListItem(List<T> items) {
        this.addAll(items);
    }

    @Override
    public void addAll(List<T> items) {
        this.items.addAll(items);
    }

    @Override
    public boolean hasNext() {
        return itemIndex < items.size();
    }


    @Override
    public T next() {
        if(hasNext()) {
            return items.get(itemIndex++);
        } else
            throw new NoSuchElementException();
    }

    @Override
    public void clear() {
        items.clear();
    }
}
