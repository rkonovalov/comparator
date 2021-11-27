package com.comparator;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class BatchQueueList<T> implements BatchQueue<T> {
    private List<BatchQueueListItem<T>> batchItems = new ArrayList<>();
    private BatchQueueItemSelectionBehaviour batchQueueItemSelectionBehaviour;
    private ReentrantLock lock = new ReentrantLock();
    private int itemIndex = 0;
    private long size = 0;

    public BatchQueueList(BatchQueueItemSelectionBehaviour batchQueueItemSelectionBehaviour) {
        this.batchQueueItemSelectionBehaviour = batchQueueItemSelectionBehaviour;
    }

    @Override
    public synchronized void add(List<T> items) {
        if(batchQueueItemSelectionBehaviour == BatchQueueItemSelectionBehaviour.ITEM_SELECT_RANDOM)
            Collections.shuffle(items);

            batchItems.add(new BatchQueueListItem<>(items));
            size += items.size();

        if(batchQueueItemSelectionBehaviour == BatchQueueItemSelectionBehaviour.ITEM_SELECT_DESC)
            itemIndex = batchItems.size() - 1;

        if(batchQueueItemSelectionBehaviour == BatchQueueItemSelectionBehaviour.ITEM_SELECT_ASC || batchQueueItemSelectionBehaviour == BatchQueueItemSelectionBehaviour.ITEM_SELECT_RANDOM)
            itemIndex = 0;
    }

    @Override
    public synchronized boolean hasNext() {
        return size > 0;
    }

    @Override
    public T next() {
        T result = null;

        switch (batchQueueItemSelectionBehaviour) {
            case ITEM_SELECT_RANDOM:
            case ITEM_SELECT_ASC:
                result = getFifoNextValue();
                break;
            case ITEM_SELECT_DESC:
                result = getFiloNextValue();
                break;
            case ITEM_SELECT_LINEAR:
                result = getAlternatelyNextValue();
                break;
        }

        return result;
    }

    public void forEach(Consumer<? super T> action) {
        T result;
        while (size > 0) {
            result = next();
            action.accept(result);
        }
    }

    private T getAlternatelyNextValue() {
        int tryCount = 0;
        while (size > 0) {
            int index = itemIndex++;
            if(itemIndex >= batchItems.size())
                itemIndex = 0;

            if (batchItems.get(index).hasNext()) {
                size--;
                return batchItems.get(index).next();
            }
            if(tryCount++ >= batchItems.size())
                break;
        }

        return null;
    }

    private T getFiloNextValue() {
        while(itemIndex >= 0) {
            if(batchItems.get(itemIndex).hasNext()) {
                size--;
                return batchItems.get(itemIndex).next();
            } else
                itemIndex--;
        }
        return null;
    }

    private T getFifoNextValue() {
            while(itemIndex < batchItems.size()) {

                if (batchItems.get(itemIndex).hasNext()) {
                    size--;
                    return batchItems.get(itemIndex).next();
                } else
                    itemIndex++;
           }
        return null;
    }

    @Override
    public boolean clear() {
        if(batchItems.stream().filter(item -> !item.hasNext()).count() == batchItems.size()) {
            lock.lock();
            try {
                batchItems.clear();
            } finally {
                lock.unlock();
            }
            return true;
        }
        return false;
    }

    public Iterator<T> iterator() {
        return this;
    }

    public Spliterator<T> spliterator() {
        if(batchQueueItemSelectionBehaviour == BatchQueueItemSelectionBehaviour.ITEM_SELECT_DESC)
            itemIndex = batchItems.size() - 1;

        return Spliterators.spliterator(this, size(), 0);
    }

    public Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    public Stream<T> parallelStream() {
        return StreamSupport.stream(spliterator(), true);
    }

    public long size() {
        return size;
    }
}
