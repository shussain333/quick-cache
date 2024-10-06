/**
 * Copyright (C) 2024, Sartaj Hussain.
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 * Project: quick-cache
 * @author sartajhussain
 */
package com.sartaj.cache.factory;

import com.sartaj.cache.config.CacheConfig;
import com.sartaj.cache.config.SingleCacheProps;
import com.sartaj.cache.exception.QuickCacheInvalidContextException;
import com.sartaj.cache.impl.InMemoryCache;
import com.sartaj.cache.model.CacheStore;
import java.util.Optional;
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
        Optional<SingleCacheProps> singleCacheProps = this.cacheConfig.getSingleProp(context);

        CacheStore<K> cacheStore =
                (CacheStore<K>)
                        singleCacheProps
                                .orElseThrow(
                                        () ->
                                                new QuickCacheInvalidContextException(
                                                        String.format("Cache store is not configured for %s", context)))
                                .getStore();

        return new InMemoryCache<>(cacheStore);
    }
}
