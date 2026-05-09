---

# 2. Core Java Integration

Quick Cache can also be used in standalone Core Java applications without Spring or Spring Boot.

This approach is useful for:
- Standalone Java applications
- CLI tools
- Lightweight services
- Background workers
- Utility applications

---

## Core Java Example

```java
import com.sartaj.cache.factory.InMemoryCacheFactory;
import com.sartaj.cache.model.CacheConfig;
import com.sartaj.cache.strategy.EvictionPolicy;

import java.util.Optional;

/**
 * Core Java example using Quick Cache
 */
public class Main {

    public static void main(String[] args) {

        /**
         * Create cache configuration
         */
        CacheConfig cacheConfig = CacheConfig.builder()
                .maxCapacity(3)
                .evictionPolicy(EvictionPolicy.LRU)
                .build();

        /**
         * Create cache factory
         */
        InMemoryCacheFactory inMemoryCacheFactory =
                new InMemoryCacheFactory();

        /**
         * Register cache context
         */
        inMemoryCacheFactory.createCache("users", cacheConfig);

        /**
         * Add data into cache
         */
        inMemoryCacheFactory
                .getInMemoryCache("users")
                .put("1", "Sartaj Hussain");

        inMemoryCacheFactory
                .getInMemoryCache("users")
                .put("2", "John Doe");

        inMemoryCacheFactory
                .getInMemoryCache("users")
                .put("3", "Jane Doe");

        /**
         * Retrieve data from cache
         */
        Optional<String> user = inMemoryCacheFactory
                .getInMemoryCache("users")
                .get("1", String.class);

        System.out.println("User: " + user.orElse("Not Found"));

        /**
         * Insert new entry to trigger eviction
         * because max capacity is 3
         */
        inMemoryCacheFactory
                .getInMemoryCache("users")
                .put("4", "Alex");

        /**
         * Verify cache content
         */
        Optional<String> evictedUser = inMemoryCacheFactory
                .getInMemoryCache("users")
                .get("2", String.class);

        System.out.println("Evicted User: " +
                evictedUser.orElse("Entry Evicted"));
    }
}
```

---

# Expected Output

```text
User: Sartaj Hussain
Evicted User: Entry Evicted
```

---

# Explanation

In the above example:

1. A cache named `users` is created.
2. Cache capacity is limited to `3`.
3. Eviction policy is configured as `LRU`.
4. Multiple entries are inserted into the cache.
5. When the fourth entry is added, the least recently used entry is automatically evicted.

This demonstrates how Quick Cache can be used independently without requiring any framework integration.

---

# Additional Documentation

- [Spring Boot Integration](./README.md#spring--spring-boot-integration)

---