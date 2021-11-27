package com.comparator;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BatchQueueListItemTest {
    private BatchQueueListItem<Integer> batchQueueListItem;
    private List<Integer> mockList = Arrays.asList(1,2,3,4,5);

    @Test
    public void testInstantiation() {
        batchQueueListItem = new BatchQueueListItem<>(mockList);

        Assert.assertTrue(batchQueueListItem.hasNext());
    }

    @Test
    public void testClear() {
        batchQueueListItem = new BatchQueueListItem<>(mockList);
        batchQueueListItem.clear();

        Assert.assertFalse(batchQueueListItem.hasNext());
    }

    @Test
    public void testIteration() {
        batchQueueListItem = new BatchQueueListItem<>(mockList);
        List<Integer> resultList = new ArrayList<>();

        while(batchQueueListItem.hasNext()) {
            resultList.add(batchQueueListItem.next());
        }


        Assert.assertEquals(mockList, resultList);
    }

}
