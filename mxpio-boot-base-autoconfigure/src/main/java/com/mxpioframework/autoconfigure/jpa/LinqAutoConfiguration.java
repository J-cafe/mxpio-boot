package com.mxpioframework.autoconfigure.jpa;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.mxpioframework.common.CommonConfiguration;
import com.mxpioframework.common.CommonConstant;
import com.mxpioframework.common.vo.ModuleVO;
import com.mxpioframework.jpa.LinqConfiguration;

import lombok.extern.slf4j.Slf4j;

/**
 * 自动装载 ORM模块 mxpio-boot-base-linq
 * @author MxpIO
 *
 */
@Configuration
@ConditionalOnClass(LinqConfiguration.class)
@AutoConfigureAfter(CommonConfiguration.class)
@Import(LinqConfiguration.class)
@Slf4j
public class LinqAutoConfiguration {
	
	public LinqAutoConfiguration() {
		log.info("[AutoConfiguration==>]:Linq Module Loading");
		CommonConstant.addModule(new ModuleVO("Linq","LinqJpa模块"));
	}

}
