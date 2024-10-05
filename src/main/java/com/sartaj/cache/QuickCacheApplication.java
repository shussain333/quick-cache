/*
 * Copyright (C) 2024, Sartaj Hussain. All rights reserved.
 * Project: quick-cache
*/
package com.sartaj.cache;

import com.sartaj.cache.config.CacheConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class QuickCacheApplication {

  private final CacheConfig cacheConfig;

  public QuickCacheApplication(CacheConfig cacheConfig) {
    this.cacheConfig = cacheConfig;
  }

  public static void main(String[] args) {
    SpringApplication.run(QuickCacheApplication.class, args);
  }

  //	@PostConstruct
  //	public void test() {
  //		System.out.println(cacheConfig.getMultiCacheProps());
  //		System.out.println(cacheConfig.getSingleCacheProps());
  //	}
}
