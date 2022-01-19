package com.mxpioframework.autoconfigure.system;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.mxpioframework.autoconfigure.security.SecurityAutoConfiguration;
import com.mxpioframework.system.SystemConfiguration;

import lombok.extern.slf4j.Slf4j;

/**
 * 自动装载 系统模块 mxpio-boot-base-system
 * @author MxpIO
 *
 */
@Configuration
@ConditionalOnClass(SystemConfiguration.class)
@AutoConfigureAfter(SecurityAutoConfiguration.class)
@Import(SystemConfiguration.class)
@Slf4j
public class SystemAutoConfiguration {
	
	public SystemAutoConfiguration() {
		log.info("[AutoConfiguration==>]:System Module Loading");
	}

}
