package com.mxpioframework.autoconfigure.common;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.mxpioframework.common.CommonConfiguration;
import com.mxpioframework.common.CommonConstant;
import com.mxpioframework.common.vo.ModuleVO;

import lombok.extern.slf4j.Slf4j;

/**
 * 自动装载 公共模块 mxpio-boot-base-common
 * @author MxpIO
 *
 */
@Configuration
@ConditionalOnClass(CommonConfiguration.class)
@Import(CommonConfiguration.class)
@Slf4j
public class CommonAutoConfiguration {
	
	public CommonAutoConfiguration() {
		log.info("[AutoConfiguration==>]:Common Module Loading");
		
		CommonConstant.addModule(new ModuleVO("Common","公共模块"));
	}

}
