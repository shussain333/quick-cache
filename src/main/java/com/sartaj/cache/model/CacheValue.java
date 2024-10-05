/**
 * Copyright (C) 2024, Sartaj Hussain.
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 * Project: quick-cache
 * @author sartajhussain
 */
package com.sartaj.cache.model;

import lombok.*;

/**
 * @author sartajhussain
 */
@Getter
@Builder(toBuilder = true, builderClassName = "Builder")
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@NonNull public class CacheValue<K> {

    /** Key identifier against a value */
    private K key;

    /** It can be any value of json */
    private String value;
}
