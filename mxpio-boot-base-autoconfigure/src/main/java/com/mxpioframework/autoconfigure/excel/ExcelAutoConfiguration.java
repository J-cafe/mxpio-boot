package com.mxpioframework.autoconfigure.excel;

import javax.annotation.Resource;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

import com.mxpioframework.autoconfigure.security.SecurityAutoConfiguration;
import com.mxpioframework.common.CommonConstant;
import com.mxpioframework.common.vo.ModuleVO;
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
	
	@Resource
	private Environment environment;
	
	public ExcelAutoConfiguration() {
		ExcelConfiguration.EXCEL_PROPERTIES.init(environment);
		log.info("[AutoConfiguration==>]:Excel Module Loading");
		CommonConstant.addModule(new ModuleVO("Excel","Excel模块"));
	}

}
