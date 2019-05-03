package com.comparator;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * This utility class simplifies if...else branching
 * <p>In situations when you need to use a lot of "else...if" statements you can simplify it by using this class.
 *
 * @param <T> value of object
 */
@SuppressWarnings("WeakerAccess")
public class Comparator<T, K> {
    private List<ComparatorItem> items;
    private T value;

    private Comparator(T value) {
        this.value = value;
        items = new ArrayList<>();
    }

    /**
     * Returns an {@code Comparator} with the specified present nullable value.
     *
     * @param value value of nullable object
     * @param <T>   Comparable object
     * @param <K>   Returnable object class
     * @return an {@code Comparator} with the value present
     */
    public static <T, K> Comparator<T, K> of(T value) {
        return new Comparator<>(value);
    }

    /**
     * Returns an {@code Comparator} with the specified present nullable value.
     *
     * @param value             value of nullable object
     * @param returnObjectClass class of object which will be returned in {@code get()} method
     * @param <T>               Comparable object
     * @param <U>               type of object
     * @return an {@code Comparator} with the value present
     */
    public static <T, U> Comparator<T, U> of(T value, Class<U> returnObjectClass) {
        if (returnObjectClass == null)
            throw new NoSuchElementException("returnObjectClass couldn't be null");
        return new Comparator<>(value);
    }

    /**
     * Adds condition and return function in items list
     *
     * @param condition   a condition to apply to the value, if present
     * @param resultValue a mapping function to apply to the value, if present
     * @return an {@code Comparator} with the value present
     * @throws NoSuchElementException if condition or mapping function is null
     */
    @SuppressWarnings("unchecked")
    public Comparator<T, K> match(Predicate<? super T> condition, Function<T, K> resultValue) {

        if (condition == null) {
            throw new NoSuchElementException("No condition present");
        } else if (resultValue == null)
            throw new NoSuchElementException("No mapping function present");

        items.add(new ComparatorItem(value, condition, resultValue));
        return this;
    }

    /**
     * Adds {@code expectedValue} and return function in items list
     *
     * @param expectedValue expected value of object
     * @param resultValue   a mapping function to apply to the value, if present
     * @param <U>           The type of the result of the mapping function
     * @return an {@code Comparator} with the value present
     * @throws NoSuchElementException if mapping function is null
     */
    @SuppressWarnings("unchecked")
    public <U> Comparator<T, K> match(Object expectedValue, Function<? super T, ? extends U> resultValue) {
        if (resultValue == null)
            throw new NoSuchElementException("No mapping function present");

        items.add(new ComparatorItem(value, (s -> s.equals(expectedValue)), resultValue));
        return this;
    }

    /**
     * Adds {@code expectedValue} and return function in items list
     *
     * @param expectedValue expected value of object
     * @param resultObject  result value of object
     * @return an {@code Comparator} with the value present
     */
    @SuppressWarnings("unchecked")
    public Comparator<T, K> match(Object expectedValue, Object resultObject) {
        items.add(new ComparatorItem(value, (s -> s.equals(expectedValue)), (s -> resultObject)));
        return this;
    }

    /**
     * Gets object from all stored in list mapping functions
     *
     * <p>This method loops through the list, pickup each item {@link ComparatorItem}.
     * If condition in item is apply to the value then method get object from mapping function.
     * If object in mapping function isn't null then method returns this object.
     * Otherwise method continues to loop list items.
     * If all items is looped and object value is null, method returns {@code defaultValue}
     *
     * @param defaultValue default value if method couln't to find not null object
     * @return {@link Object}
     */
    @SuppressWarnings("unchecked")
    private Object get(Object defaultValue) {

        for (ComparatorItem item : items) {
            Optional<T> optional = (Optional<T>) Optional.ofNullable(item.getValue());

            Object obj = optional.filter(item.getCondition())
                    .map(item.getMapper())
                    .orElse(null);
            if (obj != null)
                return obj;
        }
        return defaultValue;
    }

    /**
     * Gets object from all stored in list mapping functions.
     *
     * <p>If method couldn't to find not null object, it returns {@code value} object
     *
     * @param <U>   The type of the result of the mapping function
     * @param value not-null object which will be returned by default
     * @return object instance of U type
     */
    @SuppressWarnings("unchecked")
    public <U> U orElse(U value) {
        if (value == null) {
            throw new NoSuchElementException("No value present");
        }
        return (U) get(value);
    }

    /**
     * Gets object from all stored in list mapping functions.
     *
     * <p>If method couldn't to find not null object, it returns null value
     *
     * @param <U> The type of the result of the mapping function
     * @return object instance of U type
     */
    @SuppressWarnings("unchecked")
    public <U> U get() {
        return (U) get(null);
    }
}
