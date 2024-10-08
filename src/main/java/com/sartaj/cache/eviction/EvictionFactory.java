/**
 * Copyright (C) 2024, Sartaj Hussain.
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 * Project: quick-cache
 * @author sartajhussain
 */
package com.sartaj.cache.eviction;

import com.sartaj.cache.eviction.impl.FIFOEvictionImpl;
import com.sartaj.cache.eviction.impl.LFUEvictionImpl;
import com.sartaj.cache.eviction.impl.LRUEvictionImpl;
import com.sartaj.cache.eviction.impl.MRUEvictionImpl;
import java.util.Objects;

public class EvictionFactory {

    private static final int INITIAL_CAPACITY = 1;

    public static <K> IEviction<K> getEviction(EvictionPolicy evictionPolicy, Integer capacity) {
        if (Objects.isNull(evictionPolicy)) {
            return null;
        }
        switch (evictionPolicy) {
            case LFU -> {
                return LFUEvictionImpl.<K>builder()
                        .capacity(Integer.max(INITIAL_CAPACITY, capacity))
                        .build();
            }
            case MRU -> {
                return MRUEvictionImpl.<K>builder()
                        .capacity(Integer.max(INITIAL_CAPACITY, capacity))
                        .build();
            }
            case FIFO -> {
                return FIFOEvictionImpl.<K>builder()
                        .capacity(Integer.max(INITIAL_CAPACITY, capacity))
                        .build();
            }
            default -> {
                return LRUEvictionImpl.<K>builder()
                        .capacity(Integer.max(INITIAL_CAPACITY, capacity))
                        .build();
            }
        }
    }
}
