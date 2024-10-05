/**
 * Copyright (C) 2024, Sartaj Hussain.
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 * Project: quick-cache
 * @author sartajhussain
 */
package com.sartaj.cache.factory;

import com.sartaj.cache.config.CacheConfig;
import com.sartaj.cache.impl.InMemoryCache;
import com.sartaj.cache.model.CacheStore;
import org.springframework.stereotype.Component;

/**
 * @author sartajhussain
 */
@Component
public class InMemoryCacheFactory {

    private final CacheConfig cacheConfig;

    public InMemoryCacheFactory(CacheConfig cacheConfig) {
        this.cacheConfig = cacheConfig;
    }

    public <K> InMemoryCache<K> getInMemoryCache(String context) {
        CacheStore<K> cacheStore = (CacheStore<K>) cacheConfig.getSingleProp(context).getStore();
        return new InMemoryCache<>(cacheStore);
    }
}
