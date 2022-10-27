package com.mxpioframework.autoconfigure.module.cache.caffeine;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.mxpioframework.autoconfigure.security.SecurityAutoConfiguration;
import com.mxpioframework.cache.CacheConfiguration;
import com.mxpioframework.common.CommonConstant;
import com.mxpioframework.common.vo.ModuleVO;
import com.mxpioframework.module.cache.caffeine.CaffeineCacheConfiguration;

import lombok.extern.slf4j.Slf4j;

/**
 * 自动装载 缓存模块Caffeine实现 mxpio-boot-module-cache-caffeine
 * @author MxpIO
 *
 */
@Configuration
@ConditionalOnClass(CaffeineCacheConfiguration.class)
@AutoConfigureAfter(CacheConfiguration.class)
@AutoConfigureBefore(SecurityAutoConfiguration.class)
@Import(CaffeineCacheConfiguration.class)
@Slf4j
public class CaffeineCacheAutoConfiguration {
	
	public CaffeineCacheAutoConfiguration() {
		log.info("[AutoConfiguration==>]:CaffeineCache Module Loading");
		CommonConstant.addModule(new ModuleVO("CaffeineCache","Caffeine缓存模块"));
	}

}
