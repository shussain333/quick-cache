/**
 * Copyright (C) 2024, Sartaj Hussain.
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 * Project: quick-cache
 * @author sartajhussain
 */
package com.sartaj.cache;

import static org.junit.jupiter.api.Assertions.*;

import com.sartaj.cache.factory.InMemoryCacheFactory;
import com.sartaj.cache.impl.InMemoryCache;
import com.sartaj.cache.internal.QuickCacheApplication;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author sartajhussain
 */
@SpringBootTest(classes = QuickCacheApplication.class)
class QuickCacheApplicationTests {

    @Autowired InMemoryCacheFactory inMemoryCacheFactory;

    @Test
    public void testMultipleCacheCreated() {
        UUID key = UUID.randomUUID();
        InMemoryCache<UUID> inMemoryCacheUser = inMemoryCacheFactory.getInMemoryCache("user");

        Optional<String> expected = inMemoryCacheUser.put(key, "value");
        Optional<String> actual = inMemoryCacheUser.get(key, String.class);

        assertEquals(expected, actual);
    }

    @Test
    public void testShouldAbleToPerformPutAndGetOperationUsingDifferentRefSameContext() {
        UUID key = UUID.randomUUID();

        InMemoryCache<UUID> inMemoryCacheUserPut = inMemoryCacheFactory.getInMemoryCache("user");
        Optional<String> expected = inMemoryCacheUserPut.put(key, "value");

        InMemoryCache<UUID> inMemoryCacheUserGet = inMemoryCacheFactory.getInMemoryCache("user");
        Optional<String> actual = inMemoryCacheUserGet.get(key, String.class);

        assertEquals(expected, actual);
    }

    @Test
    public void testShouldAbleToPerformPutAndGetOperationUsingDifferentRefDifferentContext() {
        UUID key = UUID.randomUUID();

        InMemoryCache<UUID> inMemoryCacheUserPut = inMemoryCacheFactory.getInMemoryCache("user");
        Optional<String> expected = inMemoryCacheUserPut.put(key, "value");
        assertFalse(expected.isEmpty());
        assertEquals(expected.get(), "value");

        InMemoryCache<UUID> inMemoryCacheUserGet = inMemoryCacheFactory.getInMemoryCache("admin");
        Optional<String> actual = inMemoryCacheUserGet.get(key, String.class);

        assertTrue(actual.isEmpty());
    }
}
