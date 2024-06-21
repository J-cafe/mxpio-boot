package com.mxpioframework.autoconfigure.module.cache.redis;

import com.mxpioframework.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.mxpioframework.autoconfigure.security.SecurityAutoConfiguration;
import com.mxpioframework.cache.CacheConfiguration;
import com.mxpioframework.common.CommonConstant;
import com.mxpioframework.common.vo.ModuleVO;
import com.mxpioframework.module.cache.redis.RedisCacheConfiguration;

import lombok.extern.slf4j.Slf4j;

/**
 * 自动装载 缓存模块Redis实现 mxpio-boot-module-cache-redis
 * @author MxpIO
 *
 */
@Configuration
@ConditionalOnClass(RedisCacheConfiguration.class)
@AutoConfigureAfter(CacheAutoConfiguration.class)
@AutoConfigureBefore(SecurityAutoConfiguration.class)
@Import(RedisCacheConfiguration.class)
@Slf4j
public class RedisCacheAutoConfiguration {

	public RedisCacheAutoConfiguration() {
		log.info("[AutoConfiguration==>]:RedisCache Module Loading");
		CommonConstant.addModule(new ModuleVO("RedisCache","Redis缓存模块"));
	}

}
