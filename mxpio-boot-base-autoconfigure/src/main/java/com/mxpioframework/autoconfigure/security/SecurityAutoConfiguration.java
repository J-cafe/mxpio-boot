package com.mxpioframework.autoconfigure.security;

import com.mxpioframework.autoconfigure.jpa.LinqAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.mxpioframework.common.CommonConstant;
import com.mxpioframework.common.vo.ModuleVO;
import com.mxpioframework.jpa.LinqConfiguration;
import com.mxpioframework.security.SecurityConfiguration;

import lombok.extern.slf4j.Slf4j;

/**
 * 自动装载 权限模块 mxpio-boot-base-security
 * @author MxpIO
 *
 */
@Configuration
@ConditionalOnClass(SecurityConfiguration.class)
@AutoConfigureAfter(LinqAutoConfiguration.class)
@Import(SecurityConfiguration.class)
@Slf4j
public class SecurityAutoConfiguration {

	public static final String KEY_GENERATOR_BEAN_NAME = "securityKeyGenerator";

	public SecurityAutoConfiguration() {
		log.info("[AutoConfiguration==>]:Security Module Loading");
		CommonConstant.addModule(new ModuleVO("Security","权鉴模块"));
	}

	@ConditionalOnClass(SimpleKeyGenerator.class)
	@ConditionalOnMissingBean(name = KEY_GENERATOR_BEAN_NAME)
	protected static class CacheConfiguration {

		@Bean
		public SecurityKeyGenerator securityKeyGenerator() {
			return new SecurityKeyGenerator();
		}
	}

}
