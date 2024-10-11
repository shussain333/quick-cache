/**
 * Copyright (C) 2024, Sartaj Hussain.
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 * Project: quick-cache
 * @author sartajhussain
 */
package com.sartaj.cache.eviction;

public enum EvictionPolicy {
    LRU,
    FIFO,
    LFU,
    TTL,
    MRU
}
