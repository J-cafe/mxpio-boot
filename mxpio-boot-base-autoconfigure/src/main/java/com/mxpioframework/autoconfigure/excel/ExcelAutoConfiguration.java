package com.mxpioframework.autoconfigure.excel;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.mxpioframework.autoconfigure.security.SecurityAutoConfiguration;
import com.mxpioframework.excel.ExcelConfiguration;

import lombok.extern.slf4j.Slf4j;

/**
 * 自动装载 系统模块 mxpio-boot-base-excle
 * @author MxpIO
 *
 */
@Configuration
@ConditionalOnClass(ExcelConfiguration.class)
@AutoConfigureAfter(SecurityAutoConfiguration.class)
@Import(ExcelConfiguration.class)
@Slf4j
public class ExcelAutoConfiguration {
	
	public ExcelAutoConfiguration() {
		log.info("[AutoConfiguration==>]:Excel Module Loading");
	}

}
