package com.example.employee_management.config;

import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

/**
 * Cấu hình Caffeine Cache.
 * Cache "totalEmployees" sẽ tự động hết hạn sau 1 phút (TTL).
 */
@Configuration
public class CacheConfig {

    public static final String CACHE_TOTAL_EMPLOYEES = "totalEmployees";

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager(CACHE_TOTAL_EMPLOYEES);
        manager.setCaffeine(
            Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.MINUTES) // TTL = 1 phút
                .maximumSize(100)
        );
        return manager;
    }
}
