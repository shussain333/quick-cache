/**
 * Copyright (C) 2024, Sartaj Hussain.
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 * Project: quick-cache
 * @author sartajhussain
 */
package com.sartaj.cache.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Builder;
import lombok.Getter;

/**
 * @author sartajhussain
 */
@Builder(builderClassName = "Builder", toBuilder = true)
@Getter
public class CacheStore<K> {

    private final Integer capacity;

    private Map<K, CacheValue<K>> cache;
    private LinkedHashSet<K> keyTrack;
    private ObjectMapper objectMapper;

    public static class Builder<K> {
        public CacheStore<K> build() {
            cache = new ConcurrentHashMap<>(capacity);
            keyTrack = new LinkedHashSet<>(capacity);
            objectMapper = new ObjectMapper();

            return new CacheStore<>(capacity, cache, keyTrack, objectMapper);
        }
    }
}
