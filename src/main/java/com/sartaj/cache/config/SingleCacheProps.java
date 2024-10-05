/**
 * Copyright (C) 2024, Sartaj Hussain.
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 * Project: quick-cache
 * @author sartajhussain
 */
package com.sartaj.cache.config;

import com.sartaj.cache.model.CacheStore;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author sartajhussain
 */
@Configuration
@ConfigurationProperties(prefix = "com.sartaj.quick-cache.default")
@ConditionalOnProperty(prefix = "com.sartaj.quick-cache.default", name = "maxCapacity")
@Getter
public class SingleCacheProps {

    @Value("${maxCapacity: 50}")
    private Integer maxCapacity;

    private CacheStore<?> store;

    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
        store = createStore();
    }

    private <K> CacheStore<K> createStore() {
        return CacheStore.<K>builder().capacity(maxCapacity).build();
    }
}
