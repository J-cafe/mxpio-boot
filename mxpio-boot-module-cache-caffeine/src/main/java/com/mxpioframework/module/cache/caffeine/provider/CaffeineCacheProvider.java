package com.mxpioframework.module.cache.caffeine.provider;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.mxpioframework.cache.provider.CacheProvider;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


@Component
public class CaffeineCacheProvider implements CacheProvider {

    private static final int init = 1;  //缓存容量初始大小
    private static final int max = 2000;  //缓存实体的最大数目

    private static final Long SIMPLE_KEY = -1L;  //默认Cache key

    private Map<Long, LoadingCache<String, Object>> cacheMap = new ConcurrentHashMap<>(16);
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock readLock = rwLock.readLock();
    private final Lock writeLock = rwLock.writeLock();

    @Override
    public boolean hasKey(String key) {
        readLock.lock();
        try{
            for(Map.Entry<Long, LoadingCache<String, Object>> entry : cacheMap.entrySet()){
                if(entry.getValue().getIfPresent(key)!=null){
                    return true;
                }
            }
        }
        finally {
            readLock.unlock();
        }
        return false;

    }

    @Override
    public Object get(String key) {
        readLock.lock();
        try{
            for(Map.Entry<Long, LoadingCache<String, Object>> entry : cacheMap.entrySet()){
                Object obj = entry.getValue().getIfPresent(key);
                if(obj!=null){
                    return obj;
                }
            }
        }
        finally {
            readLock.unlock();
        }
        return null;
    }

    @Override
    public void del(String... keys) {
        writeLock.lock();
        try{
            for(Map.Entry<Long, LoadingCache<String, Object>> entry : cacheMap.entrySet()){
                for(String key:keys){
                    Object obj = entry.getValue().getIfPresent(key);
                    if(obj!=null){
                        entry.getValue().invalidate(key);
                    }
                }
            }
        }
        finally {
            writeLock.unlock();
        }
    }

    @Override
    public boolean set(String key, Object value) {
        writeLock.lock();
        try{
            del(key);
            LoadingCache<String, Object> simpleCache = cacheMap.get(SIMPLE_KEY);
            if(simpleCache == null){
                simpleCache = Caffeine.newBuilder()
                        .initialCapacity(init)
                        .maximumSize(max)
                        .expireAfterWrite(Long.MAX_VALUE, TimeUnit.MILLISECONDS)
                        .build(k -> null);
                cacheMap.put(SIMPLE_KEY,simpleCache);
            }
            simpleCache.put(key,value);
        }
        finally {
            writeLock.unlock();
        }
        return false;
    }

    @Override
    public boolean set(String key, Object value, long time) {
        return set(key,value,time,TimeUnit.SECONDS);
    }

    @Override
    public boolean set(String key, Object value, long time, TimeUnit timeUnit) {
        writeLock.lock();
        try{
            del(key);
            Long cacheMapKey = timeUnit.toSeconds(time);
            LoadingCache<String, Object> simpleCache = cacheMap.get(cacheMapKey);
            if(simpleCache == null){
                simpleCache = Caffeine.newBuilder()
                        .initialCapacity(init)
                        .maximumSize(max)
                        .expireAfterWrite(cacheMapKey, TimeUnit.MILLISECONDS)
                        .build(k -> null);
                cacheMap.put(cacheMapKey,simpleCache);
            }
            simpleCache.put(key,value);
        }
        finally {
            writeLock.unlock();
        }
        return false;
    }
}
