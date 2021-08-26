package com.mxpioframework.autoconfigure.module.cache.redis;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.mxpioframework.autoconfigure.security.SecurityAutoConfiguration;
import com.mxpioframework.cache.CacheConfiguration;
import com.mxpioframework.module.cache.redis.RedisCacheConfiguration;

import lombok.extern.slf4j.Slf4j;

/**
 * 自动装载 缓存模块Redis实现 mxpio-boot-module-cache-redis
 * @author MxpIO <i@mxpio.com>
 *
 */
@Configuration
@ConditionalOnClass(RedisCacheConfiguration.class)
@AutoConfigureAfter(CacheConfiguration.class)
@AutoConfigureBefore(SecurityAutoConfiguration.class)
@Import(RedisCacheConfiguration.class)
@Slf4j
public class RedisCacheAutoConfiguration {
	
	public RedisCacheAutoConfiguration() {
		log.info("[AutoConfiguration==>]:RedisCache Module Loading");
	}

}
