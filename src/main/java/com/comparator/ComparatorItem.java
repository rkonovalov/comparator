package com.comparator;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Class container which used in {@link Comparator}
 * @param <T> type of the object
 * @param <U> The type of the result of the mapping function
 */
@SuppressWarnings("WeakerAccess")
public class ComparatorItem<T, U> {
    private T value;
    private Predicate<T> condition;
    private Function<T, U> mapper;

    /**
     * Constructor
     *
     * @param value value of object
     * @param condition a condition to apply to the value, if present
     * @param mapper a mapping function to apply to the value, if present
     */
    public ComparatorItem(T value, Predicate<T> condition, Function<T, U> mapper) {
        this.value = value;
        this.condition = condition;
        this.mapper = mapper;
    }

    /**
     * Returns object value
     *
     * @return object
     */
    public T getValue() {
        return value;
    }

    /**
     * Returns condition
     *
     * @return a condition that represents the logical negation of this
     * condition
     */
    public Predicate<T> getCondition() {
        return condition;
    }

    /**
     * Returns mapper function
     *
     * @return a mapping function to apply to the value, if present
     */
    public Function<T, U> getMapper() {
        return mapper;
    }
}