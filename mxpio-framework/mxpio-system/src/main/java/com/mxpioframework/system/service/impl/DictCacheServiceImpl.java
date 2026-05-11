package com.mxpioframework.system.service.impl;

import com.mxpioframework.cache.provider.CacheProvider;
import com.mxpioframework.system.service.DictCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("mxpio.system.dictCacheService")
public class DictCacheServiceImpl implements DictCacheService {
	@Autowired
	private CacheProvider cacheProvider;

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Object key) {
		return (T) cacheProvider.get(key.toString());
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T put(Object key, T value) {
		cacheProvider.set(key.toString(), value);
		return value;
	}

	@Override
	public void remove(Object key) {
		cacheProvider.del(key.toString());
	}
}
