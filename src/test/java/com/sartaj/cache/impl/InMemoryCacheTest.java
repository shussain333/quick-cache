/**
 * Copyright (C) 2024, Sartaj Hussain.
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 * Project: quick-cache
 * @author sartajhussain
 */
package com.sartaj.cache.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sartaj.cache.eviction.IEviction;
import com.sartaj.cache.eviction.impl.LRUEvictionImpl;
import com.sartaj.cache.impl.model.Sample;
import com.sartaj.cache.model.CacheStore;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author sartajhussain
 */
public class InMemoryCacheTest {

    private InMemoryCache<String> inMemoryCache;

    @BeforeEach
    public void setUp() {
        IEviction<String> eviction = LRUEvictionImpl.<String>builder().capacity(20).build();
        CacheStore<String> cacheStore =
                CacheStore.<String>builder().capacity(20).eviction(eviction).build();
        inMemoryCache = new InMemoryCache<>(cacheStore);
    }

    @AfterEach
    public void tearDown() {
        inMemoryCache.purge();
    }

    @Test
    public void testIfStoreIsAbleToStoreInitialValue() {
        UUID orderId = UUID.randomUUID();
        UUID expected = inMemoryCache.put("orderId", orderId).orElseThrow();
        assertEquals(expected, orderId);
    }

    @Test
    public void testIfStoreDataIsRetrieved() {
        UUID orderId = UUID.randomUUID();
        Optional<UUID> expected = inMemoryCache.put("orderId", orderId);
        Optional<UUID> actual = inMemoryCache.get("orderId", UUID.class);

        assertEquals(expected, actual);
    }

    @Test
    public void testIfStoredDataRemoved() {
        UUID orderId = UUID.randomUUID();
        Optional<UUID> expected = inMemoryCache.put("orderId", orderId);
        Optional<UUID> actual = inMemoryCache.get("orderId", UUID.class);

        assertEquals(expected, actual);

        Optional<UUID> removedActual = inMemoryCache.remove("orderId", UUID.class);

        assertEquals(expected, removedActual);
        assertEquals(0, inMemoryCache.size());
    }

    @Test
    public void testIfPurgeSuccess() {

        IntStream.range(0, 200)
                .forEachOrdered(
                        value -> {
                            UUID orderId = UUID.randomUUID();
                            inMemoryCache.put(orderId.toString(), value);
                        });

        assertEquals(inMemoryCache.capacity(), inMemoryCache.size());
        inMemoryCache.purge();
        assertEquals(0, inMemoryCache.size());
    }

    @Test
    public void testIfStoringAClassObjectThenItShouldAllow() {
        UUID orderId = UUID.randomUUID();

        Sample cacheValue =
                Sample.builder().id(UUID.randomUUID()).name("Alex").date(new Date()).build();
        Optional<Sample> expected = inMemoryCache.put(orderId.toString(), cacheValue);
        Optional<Sample> actual = inMemoryCache.get(orderId.toString(), Sample.class);

        assertEquals(expected, actual);
    }
}
