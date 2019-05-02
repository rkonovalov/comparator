package com.comparator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class ComparatorTest {
    private static final String ATTRIBUTE_TEST1 = "TEST1";
    private static final String ATTRIBUTE_TEST2 = "TEST2";
    private static final String ATTRIBUTE_UNKNOWN = "UNKNOWN";
    private static final String DEFAULT = "default";
    private static final String RESULT_1 = "result_1";
    private static final String RESULT_2 = "result_2";

    private Map<String, String> items;

    @Before
    public void init() {
        items = new HashMap<>();
        items.put(ATTRIBUTE_TEST1, RESULT_1);
        items.put(ATTRIBUTE_TEST2, RESULT_2);
    }

    @Test
    public void testComparatorFirst() {

        Object result = Comparator.of(items)
                .match((s -> s.containsKey(ATTRIBUTE_TEST1)), (s -> s.get(ATTRIBUTE_TEST1)))
                .get();

        Assert.assertEquals(RESULT_1, result);
    }

    @Test
    public void testComparatorSecond() {

        Object result = Comparator.of(items)
                .match((s -> s.containsKey(ATTRIBUTE_TEST2)), (s -> s.get(ATTRIBUTE_TEST2)))
                .get();

        Assert.assertEquals(RESULT_2, result);
    }

    @Test
    public void testComparatorFirstMultiple() {

        Object result = Comparator.of(items)
                .match((s -> s.containsKey(ATTRIBUTE_UNKNOWN)), (s -> s.get(ATTRIBUTE_UNKNOWN)))
                .match((s -> s.containsKey(ATTRIBUTE_TEST2)), (s -> s.get(ATTRIBUTE_TEST2)))
                .get();

        Assert.assertEquals(RESULT_2, result);
    }

    @Test
    public void testComparatorUnknown() {


        Object result = Comparator.of(items)
                .match((s -> s.containsKey(ATTRIBUTE_UNKNOWN)), (s -> s.get(ATTRIBUTE_UNKNOWN)))
                .get();

        Assert.assertNull(result);
    }

    @Test
    public void testComparatorDefaultOnNull() {
        Object result = Comparator.of(items)
                .match((s -> s.containsKey(ATTRIBUTE_UNKNOWN)), (s -> s.get(ATTRIBUTE_UNKNOWN)))
                .orElse(DEFAULT);

        Assert.assertEquals(DEFAULT, result);
    }

    @Test
    public void testComparatorNull() {
        Map<String, String> nullItems = null;
        Object result = Comparator.of(nullItems)
                .match((s -> s.containsKey(ATTRIBUTE_TEST1)), (s -> s.get(ATTRIBUTE_TEST1)))
                .orElse(DEFAULT);

        Assert.assertEquals(DEFAULT, result);
    }

    @Test(expected = NoSuchElementException.class)
    public void testComparatorNullPredicate() {
        Object result = Comparator.of(items)
                .match(null, (s -> s.get(ATTRIBUTE_TEST1)))
                .orElse(DEFAULT);
        Assert.assertEquals(DEFAULT, result);
    }

    @Test(expected = NoSuchElementException.class)
    public void testComparatorNullMapper() {
        Object result = Comparator.of(items)
                .match((s -> s.containsKey(ATTRIBUTE_TEST1)), null)
                .orElse(DEFAULT);

        Assert.assertEquals(DEFAULT, result);
    }

    @Test
    public void testComparatorComparingObjectValue() {

        Object result = Comparator.of(items.get(ATTRIBUTE_TEST1))
                .match(RESULT_1, (s -> addSuffix("Found result 1")))
                .match(RESULT_2, (s -> addSuffix("Found result 2")))
                .get();

        Assert.assertEquals("Found result 1 OK", result);
    }

    @Test
    public void testComparatorComparingObject() {

        Object result = Comparator.of(items.get(ATTRIBUTE_TEST1))
                .match(RESULT_1, "Found result 1")
                .match(RESULT_2, "Found result 2")
                .get();

        Assert.assertEquals("Found result 1", result);
    }

    private String addSuffix(String value) {
        return value + " OK";
    }
}
