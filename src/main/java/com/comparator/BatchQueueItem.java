package com.comparator;

import java.util.Iterator;
import java.util.List;

public interface BatchQueueItem<T> extends Iterator<T> {
    void addAll(List<T> items);
    void clear();
}
