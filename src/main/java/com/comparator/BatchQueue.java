package com.comparator;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public interface BatchQueue<T> extends Iterator<T> {
    void add(List<T> items);
    boolean clear();
    void forEach(Consumer<? super T> action);
}
