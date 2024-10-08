/**
 * Copyright (C) 2024, Sartaj Hussain.
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 * Project: quick-cache
 * @author sartajhussain
 */
package com.sartaj.cache.config;

import com.sartaj.cache.exception.QuickCacheConflictException;
import com.sartaj.cache.exception.QuickCacheInvalidContextException;
import com.sartaj.cache.model.AppCache;
import jakarta.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
        log.info("=======================================================================");
        logStatistics().forEach(log::info);
        log.info("=======================================================================");
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
        multiCacheProps.ifPresent(mc -> getStringSingleCachePropsMap(context, mc));
        return multiCacheProps.map(cacheProps -> cacheProps.getMultiCache().get(context));
    }

    private static Map<String, SingleCacheProps> getStringSingleCachePropsMap(
            String context, MultiCacheProps mc) {
        return Optional.ofNullable(mc.getMultiCache())
                .orElseThrow(
                        () ->
                                new QuickCacheInvalidContextException(
                                        String.format(
                                                "Given context %s is invalid and there is no cache store present"
                                                        + " for this. Please check configuration file, if you have"
                                                        + " enabled cache for property"
                                                        + " com.sartaj.quick-cache.multiCache.%s",
                                                context, context)));
    }

    public List<AppCache> toList() {
        Stream.Builder<AppCache> cacheStream = Stream.builder();
        singleCacheProps.ifPresent(
                defaultCache -> {
                    SingleCacheProps singleCache = singleCacheProps.get();
                    cacheStream.add(
                            AppCache.builder()
                                    .context(defaultContext)
                                    .capacity(singleCache.getMaxCapacity())
                                    .evictionPolicy(singleCache.getEvictionPolicy())
                                    .build());
                });

        if (multiCacheProps.isPresent() && Objects.nonNull(multiCacheProps.get().getMultiCache())) {
            multiCacheProps.get().getMultiCache().entrySet().stream()
                    .map(
                            entry ->
                                    AppCache.builder()
                                            .context(entry.getKey())
                                            .capacity(entry.getValue().getMaxCapacity())
                                            .evictionPolicy(entry.getValue().getEvictionPolicy())
                                            .build())
                    .forEach(cacheStream::add);
        }

        return cacheStream.build().toList();
    }

    private List<String> logStatistics() {
        return toList().stream().map(AppCache::toString).collect(Collectors.toList());
    }
}
