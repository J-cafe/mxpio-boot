package com.mxpio.mxpioboot.autoconfigure.module.cache.redis;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.mxpio.mxpioboot.cache.CacheConfiguration;
import com.mxpio.mxpioboot.module.cache.redis.RedisCacheConfiguration;

import lombok.extern.slf4j.Slf4j;

@Configuration
@ConditionalOnClass(RedisCacheConfiguration.class)
@AutoConfigureAfter(CacheConfiguration.class)
@Import(RedisCacheConfiguration.class)
@Slf4j
public class RedisCacheAutoConfiguration {
	
	public RedisCacheAutoConfiguration() {
		log.info("[AutoConfiguration==>]:RedisCache Module Loading");
	}

}
