package com.comparator;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.ReentrantLock;

public class BatchQueueListItem<T> implements BatchQueueItem<T> {
    private List<T> items = new ArrayList<>();
    private int itemIndex;
    private ReentrantLock lock;

    public BatchQueueListItem(List<T> items) {
        lock = new ReentrantLock();
        this.addAll(items);
    }

    @Override
    public void addAll(List<T> items) {
        this.items.addAll(items);
    }

    @Override
    public boolean hasNext() {
        lock.lock();
        try {
            return itemIndex < items.size();
        } finally {
            lock.unlock();
        }
    }


    @Override
    public T next() {
        if(hasNext()) {
            lock.lock();
            try {
                return items.get(itemIndex++);
            } finally {
                lock.unlock();
            }
        } else
            throw new NoSuchElementException();
    }

    @Override
    public void clear() {
        items.clear();
    }
}
