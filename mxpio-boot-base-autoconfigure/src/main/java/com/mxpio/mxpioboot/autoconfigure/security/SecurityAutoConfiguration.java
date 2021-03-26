package com.mxpio.mxpioboot.autoconfigure.security;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.mxpio.mxpioboot.jpa.LinqConfiguration;
import com.mxpio.mxpioboot.security.SecurityConfiguration;

import lombok.extern.slf4j.Slf4j;

@Configuration
@ConditionalOnClass(SecurityConfiguration.class)
@AutoConfigureAfter(LinqConfiguration.class)
@Import(SecurityConfiguration.class)
@Slf4j
public class SecurityAutoConfiguration {
	
	public SecurityAutoConfiguration() {
		log.info("[AutoConfiguration==>]:Security Module Loading");
	}

}
