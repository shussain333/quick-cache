# Quick Cache

Quick Cache is a lightweight in-memory caching library built using Java 17, with backward compatibility support for Java 8 applications. It is designed for high-performance local caching within a single application instance (pod/server).

> **Note:** Quick Cache is **not a distributed cache**.

It is best suited for:
- Frequently accessed static data loaded during application startup
- Limited dynamic datasets that can fit within application memory
- Reducing repetitive database or API calls
- Improving application response time

Quick Cache can be integrated with:
- Spring Framework
- Spring Boot
- Core Java applications

The library also allows developers to configure custom cache eviction policies based on application requirements. :contentReference[oaicite:0]{index=0}

---

# Problem Statement

Modern applications frequently perform repetitive operations such as:
- Database queries
- External API calls
- Configuration lookups
- Metadata retrieval

These repeated operations increase:
- Response latency
- Database load
- Infrastructure cost

In many cases, the data changes infrequently and does not require a distributed caching solution like Redis or Hazelcast.

Quick Cache solves this problem by providing:
- Lightweight local in-memory caching
- Easy integration with Java applications
- Configurable eviction strategies
- Minimal setup and dependency overhead

This makes it ideal for:
- Microservices
- Standalone Java applications
- Internal tools
- High-read workloads with limited memory footprint

---

# Features

- Lightweight and fast in-memory caching
- Supports Java 8+ applications
- Easy Spring/Spring Boot integration
- Multiple cache eviction strategies
- Context-based multi-cache support
- Customizable cache capacity
- Simple API for put/get operations

---

# Supported Eviction Policies

By default, Quick Cache uses the **LRU (Least Recently Used)** eviction strategy.

Supported cache eviction policies:

1. LRU (Default)
2. LFU (Least Frequently Used)
3. MRU (Most Recently Used)
4. FIFO (First In First Out)

More details about cache replacement policies can be found here:

- [Cache Replacement Policies](https://en.wikipedia.org/wiki/Cache_replacement_policies)

---

# Installation

Quick Cache supports multiple build tools:

1. Apache Maven
2. Gradle
3. Gradle Kotlin DSL
4. sbt
5. ivy
6. grape
7. leiningen
8. buildr

## Maven Dependency

```xml
<dependency>
    <groupId>io.github.shussain333</groupId>
    <artifactId>quick-cache</artifactId>
    <version>1.0.0</version>
</dependency>
```

Latest versions and additional dependency formats are available on:

- [Maven Central - Quick Cache](https://central.sonatype.com/artifact/io.github.shussain333/quick-cache/overview)

---

# Usage Guide

Quick Cache can be used in two different ways:

1. Spring / Spring Boot Applications
2. Core Java Applications

---

# 1. Spring / Spring Boot Integration

Quick Cache supports two caching modes:

1. Single Shared Cache
2. Context-Based Multiple Caches

---

## Single Shared Cache

This configuration creates a single cache instance shared across the entire application.

### application.yml

```yaml
com:
  sartaj:
    quick-cache:
      default:
        maxCapacity: 200
        evictionPolicy: FIFO
```

### application.properties

```properties
com.sartaj.quick-cache.default.maxCapacity=200
com.sartaj.quick-cache.default.evictionPolicy=FIFO
```

---

## Example Usage

```java
import com.sartaj.cache.factory.InMemoryCacheFactory;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController("Check my status")
@RequestMapping("ping")
public class PingController {

    private final InMemoryCacheFactory inMemoryCacheFactory;

    public PingController(InMemoryCacheFactory inMemoryCacheFactory) {
        this.inMemoryCacheFactory = inMemoryCacheFactory;
    }

    @GetMapping
    @ResponseBody
    @ApiResponse(description = "If service is running fine the response some content")
    public ResponseEntity<String> ping() {

        // Store data in cache
        Optional<String> stringOptional =
                inMemoryCacheFactory
                        .getInMemoryCache("default")
                        .put("test", "Hi, I am from admin cache store");

        // Retrieve data from cache
        return ResponseEntity.ok(
                inMemoryCacheFactory
                        .getInMemoryCache("default")
                        .get("test", String.class)
                        .get()
        );
    }
}
```

---

# Context-Based Multi Cache

Quick Cache also supports multiple isolated cache contexts within the same application.

This is useful when:
- Different modules require separate cache configurations
- Different eviction policies are needed
- Memory isolation between domains is required

Example:
- Admin cache
- User cache
- Product cache

---

## application.yml

```yaml
com:
  sartaj:
    quick-cache:
      multiCache:
        admin:
          maxCapacity: 200
          evictionPolicy: FIFO

        user:
          maxCapacity: 200
          evictionPolicy: LFU
```

---

## application.properties

```properties
com.sartaj.quick-cache.multiCache.admin.maxCapacity=200
com.sartaj.quick-cache.multiCache.admin.evictionPolicy=FIFO

com.sartaj.quick-cache.multiCache.user.maxCapacity=200
com.sartaj.quick-cache.multiCache.user.evictionPolicy=LFU
```

---

## Example Usage

```java
import com.sartaj.cache.factory.InMemoryCacheFactory;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController("Check my status")
@RequestMapping("ping")
public class PingController {

    private final InMemoryCacheFactory inMemoryCacheFactory;

    public PingController(InMemoryCacheFactory inMemoryCacheFactory) {
        this.inMemoryCacheFactory = inMemoryCacheFactory;
    }

    @GetMapping
    @ResponseBody
    @ApiResponse(description = "If service is running fine the response some content")
    public ResponseEntity<String> ping() {

        // Store data in admin cache
        Optional<String> stringOptional =
                inMemoryCacheFactory
                        .getInMemoryCache("admin")
                        .put("test", "Hi, I am from admin cache store");

        // Retrieve data from admin cache
        return ResponseEntity.ok(
                inMemoryCacheFactory
                        .getInMemoryCache("admin")
                        .get("test", String.class)
                        .get()
        );
    }
}
```

---

# Recommended Use Cases

Quick Cache is ideal for:

- Application configuration caching
- Metadata caching
- Feature flag caching
- Frequently accessed lookup tables
- Static reference data
- API response caching (small datasets)
- Session-independent application data

---

# Limitations

- Not distributed
- Cache is local to a single application instance
- Data is lost on application restart
- Not suitable for large distributed systems requiring cache synchronization

---

# Best Practices

- Use reasonable `maxCapacity` values
- Avoid storing large objects unnecessarily
- Prefer LFU/LRU for read-heavy systems
- Use FIFO for predictable eviction ordering
- Keep cache keys meaningful and consistent

---

# Compatibility

| Component | Supported |
|---|---|
| Java 8 (or above) | ✅ |
| Spring Framework | ✅ |
| Spring Boot | ✅ |
| Core Java | ✅ |

---

# Additional Documentation

- [Core Java Example](./README_CORE_JAVA.md#core-java-example)

---

# License

This project is open-source and available under the applicable project license.