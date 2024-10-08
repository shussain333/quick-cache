/**
 * Copyright (C) 2024, Sartaj Hussain.
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 * Project: quick-cache
 * @author sartajhussain
 */
package com.sartaj.cache.model;

import com.sartaj.cache.eviction.EvictionPolicy;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Builder(builderClassName = "Builder", toBuilder = true)
@Getter
@EqualsAndHashCode
public class AppCache {
    private String context;
    private Integer capacity;
    private EvictionPolicy evictionPolicy;

    @Override
    public String toString() {
        return format();
    }

    private String format() {
        return StringUtils.leftPad(
                String.format(
                        "Context: %s | Capacity: %s | Eviction Policy: %s", context, capacity, evictionPolicy),
                10);
    }
}
