package com.comparator;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class BatchQueueList<T> extends AbstractQueue<T> implements BatchQueue<T> {
    private List<BatchQueueListItem<T>> batchItems;
    private BatchQueueItemSelectionBehaviour batchQueueItemSelectionBehaviour;
    private BatchQueueListIterator<T> iterator;
    private ReentrantLock queueLock;
    private int itemIndex = 0;
    private int size = 0;

    public BatchQueueList() {
        this(BatchQueueItemSelectionBehaviour.ITEM_SELECT_ASC);
    }

    public BatchQueueList(BatchQueueItemSelectionBehaviour batchQueueItemSelectionBehaviour) {
        batchItems = new ArrayList<>();
        iterator = new BatchQueueListIterator<>(this);
        queueLock = new ReentrantLock();
        this.batchQueueItemSelectionBehaviour = batchQueueItemSelectionBehaviour;
    }

    @Override
    public void add(List<T> items) {
        queueLock.lock();
        try {
            if (batchQueueItemSelectionBehaviour == BatchQueueItemSelectionBehaviour.ITEM_SELECT_RANDOM)
                Collections.shuffle(items);

            batchItems.add(new BatchQueueListItem<>(items));
            size += items.size();

            if (batchQueueItemSelectionBehaviour == BatchQueueItemSelectionBehaviour.ITEM_SELECT_DESC)
                itemIndex = batchItems.size() - 1;

            if (batchQueueItemSelectionBehaviour == BatchQueueItemSelectionBehaviour.ITEM_SELECT_ASC || batchQueueItemSelectionBehaviour == BatchQueueItemSelectionBehaviour.ITEM_SELECT_RANDOM)
                itemIndex = 0;
        } finally {
            queueLock.unlock();
        }
    }

    public boolean hasNext() {
        queueLock.lock();
        try {
            return size > 0;
        }finally {
            queueLock.unlock();
        }
    }

    public T next() {
        switch (batchQueueItemSelectionBehaviour) {
            case ITEM_SELECT_DESC:
                return getFiloNextValue();
            case ITEM_SELECT_LINEAR:
                return  getAlternatelyNextValue();
            case ITEM_SELECT_RANDOM:
            case ITEM_SELECT_ASC:
            default:
                return getFifoNextValue();
        }
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
    public boolean clearComplete() {
        if(batchItems.stream().filter(item -> !item.hasNext()).count() == batchItems.size()) {
            batchItems.clear();
            return true;
        }
        return false;
    }

    @Override
    public void clear() {
        clearComplete();
    }

    public Spliterator<T> spliterator() {
        if(batchQueueItemSelectionBehaviour == BatchQueueItemSelectionBehaviour.ITEM_SELECT_DESC)
            itemIndex = batchItems.size() - 1;

        return Spliterators.spliterator(this.iterator(), size(), 0);
    }

    public Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    public Stream<T> parallelStream() {
        return StreamSupport.stream(spliterator(), true);
    }

    @Override
    public Iterator<T> iterator() {
        return iterator;
    }

    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        throw new UnsupportedOperationException("removeIf method is not supported");
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean offer(T t) {
        throw new UnsupportedOperationException("offer method is not supported");
    }

    @Override
    public T poll() {
        return iterator.next();
    }

    @Override
    public T peek() {
        throw new UnsupportedOperationException("peek method is not supported");
    }
}
