package com.mxpioframework.autoconfigure.flowable;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.mxpioframework.autoconfigure.security.SecurityAutoConfiguration;
import com.mxpioframework.flowable.FlowableConfiguration;

import lombok.extern.slf4j.Slf4j;

/**
 * 自动装载 工作流模块 mxpio-boot-base-flowable
 * @author MxpIO
 *
 */
@Configuration
@ConditionalOnClass(FlowableConfiguration.class)
@AutoConfigureAfter(SecurityAutoConfiguration.class)
@Import(FlowableConfiguration.class)
@Slf4j
public class FlowableAutoConfiguration {
	
	public FlowableAutoConfiguration() {
		log.info("[AutoConfiguration==>]:Flowable Module Loading");
	}

}
