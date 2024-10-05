/*
 * Copyright (C) 2024, Sartaj Hussain. All rights reserved.
 * Project: quick-cache
*/
package com.sartaj.cache;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public interface ICache<K> {

  <V> Optional<V> put(K key, V value);

  <V> Optional<V> get(K key, Class<V> cacheValueType);

  <V, T> V execute(K key, Class<V> cacheValueType, T input, Function<T, V> definition);

  <V> V execute(K key, Class<V> cacheValueType, Supplier<V> supplier);

  <V> Optional<V> remove(K key, Class<V> cacheValueType);

  Integer size();

  void purge();

  Integer capacity();
}
