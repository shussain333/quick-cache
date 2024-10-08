/**
 * Copyright (C) 2024, Sartaj Hussain.
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 * Project: quick-cache
 * @author sartajhussain
 */
package com.sartaj.cache;

import com.sartaj.cache.eviction.IEviction;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author sartajhussain
 */
public interface ICache<K> {

    <V> Optional<V> put(K key, V value);

    <V> Optional<V> get(K key, Class<V> cacheValueType);

    <V, T> V execute(K key, Class<V> cacheValueType, T input, Function<T, V> definition);

    <V> V execute(K key, Class<V> cacheValueType, Supplier<V> supplier);

    <V> Optional<V> remove(K key, Class<V> cacheValueType);

    Integer size();

    void purge();

    Integer capacity();

    IEviction<K> getEvictPolicy();
}
