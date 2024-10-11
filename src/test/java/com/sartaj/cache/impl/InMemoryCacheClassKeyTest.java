/**
 * Copyright (C) 2024, Sartaj Hussain.
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 * Project: quick-cache
 * @author sartajhussain
 */
package com.sartaj.cache.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sartaj.cache.config.SingleCacheProps;
import com.sartaj.cache.eviction.EvictionPolicy;
import com.sartaj.cache.impl.model.Sample;
import com.sartaj.cache.model.CacheStore;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InMemoryCacheClassKeyTest {

    private InMemoryCache<Sample> inMemoryCache;

    @BeforeEach
    public void setUp() {
        SingleCacheProps singleCacheProps = new SingleCacheProps();
        singleCacheProps.setEvictionPolicy(EvictionPolicy.LRU);
        singleCacheProps.setMaxCapacity(20);

        CacheStore<Sample> cacheStore = (CacheStore<Sample>) singleCacheProps.getStore();
        inMemoryCache = new InMemoryCache<>(cacheStore);
    }

    @AfterEach
    public void tearDown() {
        inMemoryCache.purge();
    }

    @Test
    public void testIfUsingClassAsAKeyThenItShouldWork() {
        Date dtInput = new Date();
        Date dtOutput = new Date();
        UUID id = UUID.randomUUID();
        Sample cacheKeyInput = Sample.builder().id(id).name("Alex").date(dtInput).build();

        Sample cacheKeyOutput = Sample.builder().id(id).name("Alex").date(dtOutput).build();

        Optional<String> expected = inMemoryCache.put(cacheKeyInput, "Value for class key");
        Optional<String> actual = inMemoryCache.get(cacheKeyOutput, String.class);

        assertEquals(expected, actual);
    }
}
