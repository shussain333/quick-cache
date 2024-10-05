/*
 * Copyright (C) 2024, Sartaj Hussain. All rights reserved.
 * Project: quick-cache
*/
package com.sartaj.cache.config;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "com.sartaj.quick-cache")
@Setter
@Getter
public class MultiCacheProps {
  private Map<String, SingleCacheProps> multiCache;
}
