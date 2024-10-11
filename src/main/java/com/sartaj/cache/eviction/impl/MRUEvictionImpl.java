/**
 * Copyright (C) 2024, Sartaj Hussain.
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 * Project: quick-cache
 * @author sartajhussain
 */
package com.sartaj.cache.eviction.impl;

import com.sartaj.cache.eviction.EvictionPolicy;
import com.sartaj.cache.eviction.IEviction;
import java.util.*;
import lombok.Builder;
import lombok.Getter;

@Builder(builderClassName = "Builder", toBuilder = true)
@Getter
public class MRUEvictionImpl<K> implements IEviction<K> {

    private Integer capacity;
    private List<K> keyTrack;

    public static class Builder<K> {
        public MRUEvictionImpl<K> build() {
            keyTrack = new LinkedList<>();
            return new MRUEvictionImpl<>(capacity, keyTrack);
        }
    }

    @Override
    public Optional<K> remove() {
        if (getKeyTrack().size() == capacity) {
            K removedKey = keyTrack.remove(0);
            return Optional.of(removedKey);
        }
        return Optional.empty();
    }

    @Override
    public void add(K key) {
        keyTrack.add(key);
    }

    @Override
    public void remove(K key) {
        keyTrack.remove(key);
    }

    @Override
    public List<K> allKeys() {
        return keyTrack.stream().toList();
    }

    @Override
    public EvictionPolicy getEviction() {
        return EvictionPolicy.MRU;
    }

    @Override
    public void clear() {
        keyTrack.clear();
    }
}
