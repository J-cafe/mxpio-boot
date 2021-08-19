package com.mxpio.mxpioboot.autoconfigure.cache;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.mxpio.mxpioboot.common.cache.CacheConfiguration;

import lombok.extern.slf4j.Slf4j;

@Configuration
@ConditionalOnClass(CacheConfiguration.class)
@Import(CacheConfiguration.class)
@Slf4j
public class CacheAutoConfiguration {
	
	public CacheAutoConfiguration() {
		log.info("[AutoConfiguration==>]:Cache Module Loading");
	}

}
