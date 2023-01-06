package com.mxpioframework.system.service.impl;

import com.mxpioframework.system.service.DictCacheService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("mxpio.system.dictCacheService")
public class DictThreadLocalCacheServiceImpl implements DictCacheService {
	private ThreadLocal<Map<Object, Object>> threadLocalMap = new ThreadLocal<>();

	private Map<Object, Object> getCacheMap() {
		Map<Object, Object> cacheMap = threadLocalMap.get();
		if (cacheMap == null) {
			cacheMap = new HashMap<>();
			threadLocalMap.set(cacheMap);
		}
		return cacheMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Object key) {
		return (T) getCacheMap().get(key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T put(Object key, T value) {
		return (T) getCacheMap().put(key, value);

	}
}
