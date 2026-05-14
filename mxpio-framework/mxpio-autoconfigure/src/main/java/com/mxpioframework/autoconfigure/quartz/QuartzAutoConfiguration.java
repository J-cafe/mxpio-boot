package com.mxpioframework.autoconfigure.quartz;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.mxpioframework.autoconfigure.security.SecurityAutoConfiguration;
import com.mxpioframework.common.CommonConstant;
import com.mxpioframework.common.vo.ModuleVO;
import com.mxpioframework.quartz.QuartzConfiguration;

import lombok.extern.slf4j.Slf4j;

/**
 * 自动装载 调度模块 mxpio-boot-base-quartz
 * @author MxpIO
 *
 */
@Configuration
@ConditionalOnClass(QuartzConfiguration.class)
@AutoConfigureAfter(SecurityAutoConfiguration.class)
@Import(QuartzConfiguration.class)
@Slf4j
public class QuartzAutoConfiguration {
	
	public QuartzAutoConfiguration() {
		log.info("[AutoConfiguration==>]:Quartz Module Loading");
		CommonConstant.addModule(new ModuleVO("Quartz","任务调度模块"));
	}

}
