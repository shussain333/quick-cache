/*
 * Copyright (C) 2024, Sartaj Hussain. All rights reserved.
 * Project: quick-cache
*/
package com.sartaj.cache.impl.model;

import java.util.Date;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(builderClassName = "Builder")
@AllArgsConstructor
@NoArgsConstructor
public class Sample {

  private UUID id;
  private String name;
  private Date date;
}
