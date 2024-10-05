/*
 * Copyright (C) 2024, Sartaj Hussain. All rights reserved.
 * Project: quick-cache
*/
package com.sartaj.cache.model;

import lombok.*;

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
