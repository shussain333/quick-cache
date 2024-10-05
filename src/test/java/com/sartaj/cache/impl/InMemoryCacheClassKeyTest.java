/*
 * Copyright (C) 2024, Sartaj Hussain. All rights reserved.
 * Project: quick-cache
*/
package com.sartaj.cache.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    CacheStore<Sample> cacheStore = CacheStore.<Sample>builder().capacity(20).build();
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
