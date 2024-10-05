/*
 * Copyright (C) 2024, Sartaj Hussain. All rights reserved.
 * Project: quick-cache
*/
package com.sartaj.cache.config;

import com.sartaj.cache.exception.QuickCacheConflictException;
import jakarta.annotation.PostConstruct;
import java.util.Objects;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Getter
public class CacheConfig {

  private static final String defaultContext = "default";

  @Autowired(required = false)
  private SingleCacheProps singleCacheProps;

  @Autowired(required = false)
  private MultiCacheProps multiCacheProps;

  @PostConstruct
  public void validate() {
    if (Objects.nonNull(singleCacheProps)
        && (Objects.nonNull(multiCacheProps) && Objects.nonNull(multiCacheProps.getMultiCache()))) {
      throw new QuickCacheConflictException(
          "Either use single cache config for entire application or use multi cache for each"
              + " context. Both are not allowed at once.");
    }
  }

  /**
   * @param context
   * @return
   */
  public SingleCacheProps getSingleProp(String context) {
    return Objects.equals(context, defaultContext)
        ? singleCacheProps
        : multiCacheProps.getMultiCache().get(context);
  }
}
