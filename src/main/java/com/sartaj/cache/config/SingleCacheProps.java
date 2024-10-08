/**
 * Copyright (C) 2024, Sartaj Hussain.
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 * Project: quick-cache
 * @author sartajhussain
 */
package com.sartaj.cache.config;

import static com.sartaj.cache.eviction.EvictionFactory.getEviction;

import com.sartaj.cache.eviction.EvictionPolicy;
import com.sartaj.cache.model.CacheStore;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
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
@Setter
public class SingleCacheProps {

    //    @Value("${maxCapacity: 50}")
    private Integer maxCapacity = 50;

    //    @Value("${evictionPolicy:LRU}")
    private EvictionPolicy evictionPolicy = EvictionPolicy.LRU;

    @Setter(AccessLevel.NONE)
    private CacheStore<?> store;

    public <K> void setEvictionPolicy(EvictionPolicy evictionPolicy) {
        this.evictionPolicy = evictionPolicy;
        initializeStorage();
    }

    public <K> void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
        initializeStorage();
    }

    private <K> void initializeStorage() {
        if (Optional.ofNullable(store).isPresent()) {
            store =
                    store.toBuilder()
                            .capacity(maxCapacity)
                            .eviction(getEviction(evictionPolicy, maxCapacity))
                            .build();
        } else {
            store =
                    CacheStore.<K>builder()
                            .capacity(maxCapacity)
                            .eviction(getEviction(evictionPolicy, maxCapacity))
                            .build();
        }
    }

    @Override
    public String toString() {
        return format();
    }

    private String format() {
        return StringUtils.leftPad(
                String.format("capacity: %s | eviction policy: %s", maxCapacity, evictionPolicy), 10);
    }
}
