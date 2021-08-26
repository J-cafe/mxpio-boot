package com.mxpioframework.autoconfigure.cache;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.mxpioframework.cache.CacheConfiguration;

import lombok.extern.slf4j.Slf4j;

/**
 * 自动装载 缓存模块 mxpio-boot-base-cache
 * @author MxpIO <i@mxpio.com>
 *
 */
@Configuration
@ConditionalOnClass(CacheConfiguration.class)
@Import(CacheConfiguration.class)
@Slf4j
public class CacheAutoConfiguration {
	
	public CacheAutoConfiguration() {
		log.info("[AutoConfiguration==>]:Cache Module Loading");
	}

}
