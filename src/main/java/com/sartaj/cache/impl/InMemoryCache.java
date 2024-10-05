/*
 * Copyright (C) 2024, Sartaj Hussain. All rights reserved.
 * Project: quick-cache
*/
package com.sartaj.cache.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sartaj.cache.ICache;
import com.sartaj.cache.exception.QuickCacheJsonParseException;
import com.sartaj.cache.model.CacheStore;
import com.sartaj.cache.model.CacheValue;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;

// @Component
// @Profile("local_cache")
@Slf4j
public class InMemoryCache<K> implements ICache<K> {
  private final CacheStore<K> store;

  public InMemoryCache(CacheStore<K> store) {
    this.store = store;
  }

  @Override
  public <V> Optional<V> put(K key, V value) {
    synchronized (this) {
      if (getKeyTrack().size() == capacity()) {
        K firstKey = getKeyTrack().stream().findFirst().orElseThrow();
        getKeyTrack().remove(firstKey);
        getCache().remove(firstKey);
      }

      getCache().put(key, write(key, value));
      getKeyTrack().remove(key);
      getKeyTrack().add(key);
    }
    return (Optional<V>) get(key, value.getClass());
  }

  @Override
  public <V> Optional<V> get(K key, Class<V> cacheValueType) {
    return read(key, cacheValueType);
  }

  @Override
  public <V, T> V execute(K key, Class<V> cacheValueType, T input, Function<T, V> definition) {
    Optional<V> cacheValue = get(key, cacheValueType);

    return cacheValue.orElseGet(
        () -> {
          V value = definition.apply(input);
          put(key, value);
          return value;
        });
  }

  @Override
  public <V> V execute(K key, Class<V> cacheValueType, Supplier<V> supplier) {
    Optional<V> cacheValue = get(key, cacheValueType);

    return cacheValue.orElseGet(
        () -> {
          V value = supplier.get();
          put(key, value);
          return value;
        });
  }

  @Override
  public <V> Optional<V> remove(K key, Class<V> cacheValueType) {
    synchronized (this) {
      Optional<V> optionalV = read(key, cacheValueType);
      Optional<CacheValue<K>> kCacheValue = Optional.ofNullable(getCache().remove(key));
      getKeyTrack().remove(key);

      if (kCacheValue.isPresent()) {
        return optionalV;
      }
    }
    return Optional.empty();
  }

  @Override
  public Integer size() {
    return getCache().size();
  }

  @Override
  public void purge() {
    getKeyTrack().clear();
    getCache().clear();
  }

  @Override
  public Integer capacity() {
    return store.getCapacity();
  }

  private LinkedHashSet<K> getKeyTrack() {
    return store.getKeyTrack();
  }

  private Map<K, CacheValue<K>> getCache() {
    return store.getCache();
  }

  private ObjectMapper getObjectMapper() {
    return store.getObjectMapper();
  }

  private <V> CacheValue<K> write(K key, V cacheValue) {
    try {
      String value =
          getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(cacheValue);
      return CacheValue.<K>builder().key(key).value(value).build();
    } catch (JsonProcessingException e) {
      throw new QuickCacheJsonParseException(
          String.format("Something when wrong while writing cache for key %s", key));
    }
  }

  private <V> Optional<V> read(K key, Class<V> valueClass) {
    try {
      CacheValue<K> cacheValue = getCache().getOrDefault(key, null);

      if (Objects.isNull(cacheValue)) {
        return Optional.empty();
      }
      V val = getObjectMapper().readValue(cacheValue.getValue(), valueClass);
      return Optional.ofNullable(val);
    } catch (JsonProcessingException ex) {
      log.info("Failed to retrieve the record", ex);
      throw new RuntimeException(ex);
    }
  }
}
