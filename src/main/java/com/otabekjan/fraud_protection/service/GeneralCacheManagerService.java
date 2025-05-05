package com.otabekjan.fraud_protection.service;


import io.jmix.core.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * author: Otabek.E
 * date: 08/07/24 12:46
 */

@Service("jb_GeneralCacheManagerService")
public class GeneralCacheManagerService {

    @Autowired
    private Messages messages;
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;


    // key - cacheManager
    // value - cache names
    public Map<String, Collection<String>> getAllCacheNames() {
        Map<String, CacheManager> cacheManagers = applicationContext.getBeansOfType(CacheManager.class);

        return cacheManagers.entrySet().stream()
                .map(entry -> Map.of(entry.getKey(), entry.getValue().getCacheNames()))
                .reduce((map1, map2) -> {
                    map1.putAll(map2);
                    return map1;
                })
                .orElse(null);
    }


    public boolean clearCache(String cacheManagerName, String cacheName) {
        Map<String, CacheManager> cacheManagers = applicationContext.getBeansOfType(CacheManager.class);
        Map.Entry<String, CacheManager> cacheManagerEntry = cacheManagers.entrySet().stream()
                .filter(entry -> Objects.equals(entry.getKey(), cacheManagerName))
                .findAny().orElse(null);

        if (cacheManagerEntry == null) return false;
        Cache cache = cacheManagerEntry.getValue().getCache(cacheName);
        if (cache == null) return false;

        cache.clear();
        return true;
    }

    public static class IllegalCacheAccessException extends RuntimeException {
        public IllegalCacheAccessException(String message) {
            super(message);
        }
    }
}
