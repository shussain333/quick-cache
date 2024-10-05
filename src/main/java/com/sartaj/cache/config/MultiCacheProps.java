/**
 * Copyright (C) 2024, Sartaj Hussain.
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 * Project: quick-cache
 * @author sartajhussain
 */
package com.sartaj.cache.config;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author sartajhussain
 */
@Configuration
@ConfigurationProperties(prefix = "com.sartaj.quick-cache")
@Setter
@Getter
public class MultiCacheProps {
    private Map<String, SingleCacheProps> multiCache;
}
