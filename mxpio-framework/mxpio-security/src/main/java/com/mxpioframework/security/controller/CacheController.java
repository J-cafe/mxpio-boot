package com.mxpioframework.security.controller;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mxpioframework.cache.provider.CacheProvider;
import com.mxpioframework.common.vo.Result;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "CacheController", description = "缓存管理接口")
@RestController("mxpio.security.cacheController")
@RequestMapping("/cache/")
public class CacheController {

	@Autowired
	private CacheProvider cacheProvider;

	@Autowired
	private ObjectProvider<CacheManager> cacheManagerProvider;

	@GetMapping("clear")
	@Operation(summary = "清除系统缓存", description = "清除当前应用的所有缓存（含注解缓存与手工缓存），不影响共用缓存实例的其他应用", method = "GET")
	public Result<Long> clear() {
		long count = 0;
		CacheManager cacheManager = cacheManagerProvider.getIfAvailable();
		if (cacheManager != null) {
			for (String cacheName : cacheManager.getCacheNames()) {
				Cache cache = cacheManager.getCache(cacheName);
				if (cache != null) {
					cache.clear();
				}
			}
		}
		count += cacheProvider.clear();
		return Result.OK(count);
	}

}
