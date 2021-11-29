package com.comparator;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class BatchQueueListTest {
    BatchQueueList<Integer> batchQueueList;

    private List<Integer> mockList(int min, int max) {
        List<Integer> result = new ArrayList<>(max + 1);
        for (int i = min; i <= max; i++) {
            result.add(i);
        }
        return result;
    }
    private void insertMocInitialBatches(BatchQueueList<Integer> queue) {
        int k = 0;
        for(int n = 1; n <= 500; n++) {
            queue.add(mockList(1 + k, 50 + k));
            k += 10;
        }
    }

    @Test
    public void testDefaultConstructor() {
        batchQueueList = new BatchQueueList<>();
        insertMocInitialBatches(batchQueueList);

        List<Integer> resultList = batchQueueList.stream().sorted().collect(Collectors.toList());

        Assert.assertEquals(25000, resultList.size());
        Assert.assertEquals(63012500, resultList.stream().mapToLong(i -> i).sum());
        Assert.assertEquals(118108229, resultList.hashCode());
    }

    @Test
    public void testAscItemSelection() {
        batchQueueList = new BatchQueueList<>(BatchQueueItemSelectionBehaviour.ITEM_SELECT_ASC);
        insertMocInitialBatches(batchQueueList);

        List<Integer> resultList = batchQueueList.stream().sorted().collect(Collectors.toList());

        Assert.assertEquals(25000, resultList.size());
        Assert.assertEquals(63012500, resultList.stream().mapToLong(i -> i).sum());
        Assert.assertEquals(118108229, resultList.hashCode());
    }

    @Test
    public void testDescItemSelection() {
        batchQueueList = new BatchQueueList<>(BatchQueueItemSelectionBehaviour.ITEM_SELECT_DESC);
        insertMocInitialBatches(batchQueueList);

        List<Integer> resultList = batchQueueList.stream().sorted().collect(Collectors.toList());

        Assert.assertEquals(25000, resultList.size());
        Assert.assertEquals(63012500, resultList.stream().mapToLong(i -> i).sum());
        Assert.assertEquals(118108229, resultList.hashCode());
    }

    @Test
    public void testLinearItemSelection() {
        batchQueueList = new BatchQueueList<>(BatchQueueItemSelectionBehaviour.ITEM_SELECT_LINEAR);
        insertMocInitialBatches(batchQueueList);

        List<Integer> resultList = batchQueueList.stream().sorted().collect(Collectors.toList());

        Assert.assertEquals(25000, resultList.size());
        Assert.assertEquals(63012500, resultList.stream().mapToLong(i -> i).sum());
        Assert.assertEquals(118108229, resultList.hashCode());
    }

    @Test
    public void testRandomItemSelection() {
        batchQueueList = new BatchQueueList<>(BatchQueueItemSelectionBehaviour.ITEM_SELECT_RANDOM);
        insertMocInitialBatches(batchQueueList);

        List<Integer> resultList = batchQueueList.stream().sorted().collect(Collectors.toList());

        Assert.assertEquals(25000, resultList.size());
        Assert.assertEquals(63012500, resultList.stream().mapToLong(i -> i).sum());
        Assert.assertEquals(118108229, resultList.hashCode());
    }

    @Test
    public void testParallelAscItemSelection() {
        batchQueueList = new BatchQueueList<>(BatchQueueItemSelectionBehaviour.ITEM_SELECT_ASC);
        insertMocInitialBatches(batchQueueList);

        List<Integer> resultList = batchQueueList.parallelStream().sorted().collect(Collectors.toList());

        Assert.assertEquals(25000, resultList.size());
        Assert.assertEquals(63012500, resultList.stream().mapToLong(i -> i).sum());
        Assert.assertEquals(118108229, resultList.hashCode());
    }

    @Test
    public void testParallelDescItemSelection() {
        batchQueueList = new BatchQueueList<>(BatchQueueItemSelectionBehaviour.ITEM_SELECT_DESC);
        insertMocInitialBatches(batchQueueList);

        List<Integer> resultList = batchQueueList.parallelStream().sorted().collect(Collectors.toList());

        Assert.assertEquals(25000, resultList.size());
        Assert.assertEquals(63012500, resultList.stream().mapToLong(i -> i).sum());
        Assert.assertEquals(118108229, resultList.hashCode());
    }

    @Test
    public void testParallelLinearItemSelection() {
        batchQueueList = new BatchQueueList<>(BatchQueueItemSelectionBehaviour.ITEM_SELECT_LINEAR);
        insertMocInitialBatches(batchQueueList);

        List<Integer> resultList = batchQueueList.parallelStream().sorted().collect(Collectors.toList());

        Assert.assertEquals(25000, resultList.size());
        Assert.assertEquals(63012500, resultList.stream().mapToLong(i -> i).sum());
        Assert.assertEquals(118108229, resultList.hashCode());
    }

    @Test
    public void testParallelRandomItemSelection() {
        batchQueueList = new BatchQueueList<>(BatchQueueItemSelectionBehaviour.ITEM_SELECT_RANDOM);
        insertMocInitialBatches(batchQueueList);

        List<Integer> resultList = batchQueueList.stream().parallel().sorted().collect(Collectors.toList());

        Assert.assertEquals(25000, resultList.size());
        Assert.assertEquals(63012500, resultList.stream().mapToLong(i -> i).sum());
        Assert.assertEquals(118108229, resultList.hashCode());
    }

    @Test
    public void testParallel10Times() {
        boolean result = true;
        for (int i = 0; i < 10; i++) {
            testParallelAscItemSelection();
            testParallelDescItemSelection();
            testParallelLinearItemSelection();
            testParallelRandomItemSelection();
        }
        Assert.assertTrue(result);
    }

    @Test
    public void testParallelAddWhileIterating() {
        batchQueueList = new BatchQueueList<>(BatchQueueItemSelectionBehaviour.ITEM_SELECT_RANDOM);
        insertMocInitialBatches(batchQueueList);
        int randomValue = ThreadLocalRandom.current().nextInt(1, 5);
        List<Integer> resultList = new CopyOnWriteArrayList<>();

       batchQueueList.parallelStream().forEach(item -> {

           resultList.add(item);

            if(item.equals(randomValue))
                batchQueueList.add(mockList(10000, 10100));

            if(item.equals(randomValue + 1))
                batchQueueList.add(mockList(10200, 10300));
        });

        batchQueueList.parallelStream().forEach(resultList::add);
        Collections.sort(resultList);

        Assert.assertEquals(25202, resultList.size());
        Assert.assertEquals(65062800, resultList.stream().mapToLong(i -> i).sum());
        Assert.assertEquals(-766525939, resultList.hashCode());
    }

    @Test
    public void testParallelAddWhileIterating10Times() {
        boolean result = true;
        for (int i = 0; i < 10; i++) {
            testParallelAddWhileIterating();
        }
        Assert.assertTrue(result);
    }

    @Test
    public void testRandomItemSelectionForEach() {
        batchQueueList = new BatchQueueList<>(BatchQueueItemSelectionBehaviour.ITEM_SELECT_RANDOM);
        insertMocInitialBatches(batchQueueList);

        List<Integer> resultList = new CopyOnWriteArrayList<>();

        batchQueueList.forEach(resultList::add);
        Collections.sort(resultList);

        Assert.assertEquals(25000, resultList.size());
        Assert.assertEquals(63012500, resultList.stream().mapToLong(i -> i).sum());
        Assert.assertEquals(118108229, resultList.hashCode());
    }

    @Test
    public void testAscItemSelectionAndClean() {
        batchQueueList = new BatchQueueList<>(BatchQueueItemSelectionBehaviour.ITEM_SELECT_RANDOM);
        insertMocInitialBatches(batchQueueList);

        List<Integer> resultList1 = batchQueueList.stream().sorted().collect(Collectors.toList());
        batchQueueList.clear();

        insertMocInitialBatches(batchQueueList);
        List<Integer> resultList2 = batchQueueList.stream().sorted().collect(Collectors.toList());

        Assert.assertEquals(resultList1, resultList2);
    }

    @Test
    public void testAscItemSelectionAndIterator() {
        batchQueueList = new BatchQueueList<>(BatchQueueItemSelectionBehaviour.ITEM_SELECT_RANDOM);
        insertMocInitialBatches(batchQueueList);

        List<Integer> resultList = new CopyOnWriteArrayList<>();

        Iterator<Integer> iterator = batchQueueList.iterator();
        while(iterator.hasNext()) {
            resultList.add(iterator.next());
        }
        Collections.sort(resultList);

        Assert.assertEquals(25000, resultList.size());
        Assert.assertEquals(63012500, resultList.stream().mapToLong(i -> i).sum());
        Assert.assertEquals(118108229, resultList.hashCode());
    }

    @Test
    public void testAscItemSelectionAndSpliterator() {
        batchQueueList = new BatchQueueList<>(BatchQueueItemSelectionBehaviour.ITEM_SELECT_RANDOM);
        insertMocInitialBatches(batchQueueList);

        List<Integer> resultList = new CopyOnWriteArrayList<>();

        Spliterator<Integer> spliterator = batchQueueList.spliterator();

        spliterator.forEachRemaining(resultList::add);
        Collections.sort(resultList);

        Assert.assertEquals(25000, resultList.size());
        Assert.assertEquals(63012500, resultList.stream().mapToLong(i -> i).sum());
        Assert.assertEquals(118108229, resultList.hashCode());
    }

    @Test
    public void testIteratorNotNull() {
        batchQueueList = new BatchQueueList<>(BatchQueueItemSelectionBehaviour.ITEM_SELECT_RANDOM);
        insertMocInitialBatches(batchQueueList);

        Assert.assertNotNull(batchQueueList.iterator());
    }

    @Test
    public void testSpliteratorNotNull() {
        batchQueueList = new BatchQueueList<>(BatchQueueItemSelectionBehaviour.ITEM_SELECT_RANDOM);
        insertMocInitialBatches(batchQueueList);

        Assert.assertNotNull(batchQueueList.spliterator());
    }

    @Test
    public void testStreamNotNull() {
        batchQueueList = new BatchQueueList<>(BatchQueueItemSelectionBehaviour.ITEM_SELECT_RANDOM);
        insertMocInitialBatches(batchQueueList);

        Assert.assertNotNull(batchQueueList.stream());
    }

    @Test
    public void testClearFalse() {
        batchQueueList = new BatchQueueList<>(BatchQueueItemSelectionBehaviour.ITEM_SELECT_RANDOM);
        insertMocInitialBatches(batchQueueList);

        Assert.assertFalse(batchQueueList.clearComplete());
    }

    @Test
    public void testClearTrue() {
        batchQueueList = new BatchQueueList<>(BatchQueueItemSelectionBehaviour.ITEM_SELECT_RANDOM);
        insertMocInitialBatches(batchQueueList);

        AtomicInteger value = new AtomicInteger(0);
        batchQueueList.forEach(value::set);
        Assert.assertTrue(batchQueueList.clearComplete());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveIf() {
        batchQueueList = new BatchQueueList<>();
        batchQueueList.removeIf(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testOffer() {
        batchQueueList = new BatchQueueList<>();
        batchQueueList.offer(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPeek() {
        batchQueueList = new BatchQueueList<>();
        batchQueueList.peek();
    }

    @Test
    public void testPoll() {
        batchQueueList = new BatchQueueList<>();
        insertMocInitialBatches(batchQueueList);

        Integer value = batchQueueList.poll();

        Assert.assertNotNull(value);
        Assert.assertEquals(1, value.intValue());
    }

}
