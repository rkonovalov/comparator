package com.comparator;

import java.util.List;
import java.util.function.Consumer;

public interface BatchQueue<T> {
    void add(List<T> items);
    boolean clearComplete();
    void forEach(Consumer<? super T> action);
}
