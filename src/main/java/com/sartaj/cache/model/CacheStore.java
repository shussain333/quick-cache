/**
 * Copyright (C) 2024, Sartaj Hussain.
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 * Project: quick-cache
 * @author sartajhussain
 */
package com.sartaj.cache.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sartaj.cache.eviction.IEviction;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author sartajhussain
 */
@Builder(builderClassName = "Builder", toBuilder = true)
@Getter
@Setter
public class CacheStore<K> {

    private final Integer capacity;
    private Map<K, CacheValue<K>> cache;
    private IEviction<K> eviction;
    private ObjectMapper objectMapper;

    public static class Builder<K> {
        public CacheStore<K> build() {
            cache = new ConcurrentHashMap<>(capacity);
            objectMapper = new ObjectMapper();

            return new CacheStore<>(capacity, cache, eviction, objectMapper);
        }
    }
}
