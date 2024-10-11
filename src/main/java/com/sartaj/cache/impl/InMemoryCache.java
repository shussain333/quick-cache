/**
 * Copyright (C) 2024, Sartaj Hussain.
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 * Project: quick-cache
 * @author sartajhussain
 */
package com.sartaj.cache.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sartaj.cache.ICache;
import com.sartaj.cache.eviction.IEviction;
import com.sartaj.cache.exception.QuickCacheJsonParseException;
import com.sartaj.cache.model.CacheStore;
import com.sartaj.cache.model.CacheValue;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;

/**
 * @author sartajhussain
 */
@Slf4j
public class InMemoryCache<K> implements ICache<K> {
    private final CacheStore<K> store;

    public InMemoryCache(CacheStore<K> store) {
        Objects.requireNonNull(store, "Store is not present");
        this.store = store;
    }

    @Override
    public <V> Optional<V> put(K key, V value) {
        synchronized (this) {
            Optional<K> evictedKeyOp = getEvictPolicy().remove();
            evictedKeyOp.ifPresent(evictedKey -> getCache().remove(evictedKey));

            getCache().put(key, write(key, value));
            getEvictPolicy().add(key);
        }
        return (Optional<V>) get(key, value.getClass());
    }

    @Override
    public <V> Optional<V> get(K key, Class<V> cacheValueType) {
        Optional<V> read = read(key, cacheValueType);
        getEvictPolicy().remove(key);
        getEvictPolicy().add(key);
        return read;
    }

    @Override
    public <V, T> V execute(K key, Class<V> cacheValueType, T input, Function<T, V> definition) {
        Optional<V> cacheValue = read(key, cacheValueType);

        return cacheValue.orElseGet(
                () -> {
                    V value = definition.apply(input);
                    put(key, value);
                    return value;
                });
    }

    @Override
    public <V> V execute(K key, Class<V> cacheValueType, Supplier<V> supplier) {
        Optional<V> cacheValue = read(key, cacheValueType);

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
            getEvictPolicy().remove(key);

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
        getEvictPolicy().clear();
        getCache().clear();
    }

    @Override
    public Integer capacity() {
        return store.getCapacity();
    }

    @Override
    public IEviction<K> getEvictPolicy() {
        return store.getEviction();
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
