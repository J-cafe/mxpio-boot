package com.mxpioframework.autoconfigure.log;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.mxpioframework.autoconfigure.jpa.LinqAutoConfiguration;
import com.mxpioframework.log.LogConfiguration;

import lombok.extern.slf4j.Slf4j;

/**
 * 自动装载 工作流模块 mxpio-boot-base-log
 * @author MxpIO
 *
 */
@Configuration
@ConditionalOnClass(LogConfiguration.class)
@AutoConfigureAfter(LinqAutoConfiguration.class)
@Import(LogConfiguration.class)
@Slf4j
public class LogAutoConfiguration {
	
	public LogAutoConfiguration() {
		log.info("[AutoConfiguration==>]:Log Module Loading");
	}

}
