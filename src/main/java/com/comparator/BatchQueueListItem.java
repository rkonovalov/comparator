package com.comparator;

import java.util.ArrayList;
import java.util.List;
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

    //Need to make thread safe
    @Override
    public T next() {
        return hasNext() ? items.get(itemIndex++) : null;
    }

    @Override
    public void clear() {
        items.clear();
    }
}
