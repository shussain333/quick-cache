/**
 * Copyright (C) 2024, Sartaj Hussain.
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 * Project: quick-cache
 * @author sartajhussain
 */
package com.sartaj.cache.eviction;

import java.util.List;
import java.util.Optional;

public interface IEviction<K> {

    Optional<K> remove();

    void add(K key);

    void remove(K key);

    List<K> allKeys();

    EvictionPolicy getEviction();

    void clear();
}
