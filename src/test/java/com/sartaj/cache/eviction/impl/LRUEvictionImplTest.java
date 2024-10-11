/**
 * Copyright (C) 2024, Sartaj Hussain.
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 * Project: quick-cache
 * @author sartajhussain
 */
package com.sartaj.cache.eviction.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sartaj.cache.RandomGenerator;
import com.sartaj.cache.eviction.EvictionPolicy;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LRUEvictionImplTest {

    private LRUEvictionImpl<Integer> eviction;

    @BeforeEach
    public void setUp() {
        eviction = LRUEvictionImpl.<Integer>builder().capacity(5).build();
    }

    @AfterEach
    public void tearDown() {
        eviction.clear();
    }

    @Test
    public void testIsValidForLRU() {
        assertEquals(eviction.getEviction(), EvictionPolicy.LRU);
    }

    @Test
    public void testLeastRecentlyUsedKeyShouldBeRemoved() {
        IntStream.range(0, 5).forEachOrdered(eviction::add);

        // Make sure removing from front and putting in the beginning
        IntStream.range(1, 6)
                .forEachOrdered(
                        value -> {
                            eviction.add(value);
                            assertEquals(eviction.getKeyTrack().peek(), Objects.equals(value, 5) ? 1 : value);
                        });
    }

    @Test
    public void testIfElementsAreSentToStoreMoreThanCapacity_01() {
        IntStream.range(0, 100).forEachOrdered(eviction::add);

        assertEquals(5, eviction.getKeyTrack().size());

        List<Integer> expectedResult = IntStream.range(95, 100).boxed().toList();
        assertThat(expectedResult, contains(eviction.getKeyTrack().toArray(Integer[]::new)));
    }

    @Test
    public void testIfElementsAreSentToStoreMoreThanCapacity_02() {
        RandomGenerator<Integer> integerRandomGenerator =
                RandomGenerator.<Integer>builder().origin(1).bound(10).max(20).build();

        IntStream.range(0, 10).forEach(value -> eviction.add(integerRandomGenerator.toPositive()));

        integerRandomGenerator.toPositiveList().forEach(value -> eviction.remove(value));
        assertThat(eviction.getKeyTrack().toArray(Integer[]::new), Matchers.notNullValue());
    }

    @SneakyThrows({InterruptedException.class, Exception.class})
    @Test
    public void testWhereMultiThreadedScenarioShouldNotCorruptData() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        CopyOnWriteArrayList<Integer> results = new CopyOnWriteArrayList<>();
        CountDownLatch countDownLatch = new CountDownLatch(2);
        List<Boolean> emptyCollection = new ArrayList<>();

        IntStream.range(0, 7)
                .forEachOrdered(
                        value -> {
                            executorService.submit(
                                    () -> {
                                        eviction.add(value);
                                        countDownLatch.countDown();
                                    });
                            executorService.submit(
                                    () -> {
                                        Optional<Integer> remove = eviction.remove();
                                        remove.ifPresentOrElse(null, () -> emptyCollection.add(true));
                                        countDownLatch.countDown();
                                    });
                        });

        countDownLatch.await();
        executorService.shutdown();

        assertThat(emptyCollection, Matchers.iterableWithSize(Matchers.greaterThanOrEqualTo(0)));
    }
}
