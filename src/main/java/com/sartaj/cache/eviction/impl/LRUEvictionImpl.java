/**
 * Copyright (C) 2024, Sartaj Hussain.
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 * Project: quick-cache
 * @author sartajhussain
 */
package com.sartaj.cache.eviction.impl;

import com.sartaj.cache.eviction.EvictionPolicy;
import com.sartaj.cache.eviction.IEviction;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.LinkedBlockingQueue;
import lombok.Builder;
import lombok.Getter;

@Builder(builderClassName = "Builder", toBuilder = true)
@Getter
public class LRUEvictionImpl<K> implements IEviction<K> {

    private Integer capacity;
    private LinkedBlockingQueue<K> keyTrack;

    public static class Builder<K> {
        public LRUEvictionImpl<K> build() {
            keyTrack = new LinkedBlockingQueue<>(capacity);
            return new LRUEvictionImpl<>(capacity, keyTrack);
        }
    }

    @Override
    public Optional<K> remove() {
        if (Objects.equals(getKeyTrack().size(), capacity)) {
            K removedKey = keyTrack.poll();
            return Optional.ofNullable(removedKey);
        }
        return Optional.empty();
    }

    @Override
    public void add(K key) {
        if (Objects.equals(getKeyTrack().size(), capacity)) {
            keyTrack.poll();
        }
        keyTrack.offer(key);
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
        return EvictionPolicy.LRU;
    }

    @Override
    public void clear() {
        keyTrack.clear();
    }
}
