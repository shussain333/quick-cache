/**
 * Copyright (C) 2024, Sartaj Hussain.
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 * Project: quick-cache
 * @author sartajhussain
 */
package com.sartaj.cache.config;

import com.sartaj.cache.exception.QuickCacheConflictException;
import com.sartaj.cache.exception.QuickCacheInvalidContextException;
import jakarta.annotation.PostConstruct;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author sartajhussain
 */
@Slf4j
@Component
public class CacheConfig {

    private static final String defaultContext = "default";

    @Autowired(required = false)
    private Optional<SingleCacheProps> singleCacheProps;

    @Autowired(required = false)
    private Optional<MultiCacheProps> multiCacheProps;

    @PostConstruct
    public void validate() {
        if (singleCacheProps.isPresent()
                && (multiCacheProps.isPresent()
                        && Objects.nonNull(multiCacheProps.get().getMultiCache()))) {
            throw new QuickCacheConflictException(
                    "Either use single cache config for entire application or use multi cache for each"
                            + " context. Both are not allowed at once.");
        }
        log.info("Quick-Cache initialized successfully");
    }

    /**
     * @param context
     * @return
     */
    public Optional<SingleCacheProps> getSingleProp(String context) {
        return Objects.equals(context, defaultContext)
                ? singleCacheProps
                : getSinglePropFromMultiCache(context);
    }

    private Optional<SingleCacheProps> getSinglePropFromMultiCache(String context) {
        multiCacheProps.ifPresent(
                mc ->
                        Optional.ofNullable(mc.getMultiCache())
                                .orElseThrow(
                                        () ->
                                                new QuickCacheInvalidContextException(
                                                        String.format(
                                                                "Given context %s is invalid and there is no cache store present"
                                                                        + " for this. Please check configuration file if you have"
                                                                        + " enabled cache for property"
                                                                        + " com.sartaj.quick-cache.multiCache.%s",
                                                                context, context))));
        return multiCacheProps.map(cacheProps -> cacheProps.getMultiCache().get(context));
    }
}
