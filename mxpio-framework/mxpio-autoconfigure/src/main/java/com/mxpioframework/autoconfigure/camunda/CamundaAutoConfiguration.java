package com.mxpioframework.autoconfigure.camunda;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.mxpioframework.autoconfigure.security.SecurityAutoConfiguration;
import com.mxpioframework.camunda.CamundaConfiguration;
import com.mxpioframework.common.CommonConstant;
import com.mxpioframework.common.vo.ModuleVO;

import lombok.extern.slf4j.Slf4j;

/**
 * 自动装载 工作流模块 mxpio-boot-base-camunda
 * @author MxpIO
 *
 */
@Configuration
@ConditionalOnClass(CamundaConfiguration.class)
@AutoConfigureAfter(SecurityAutoConfiguration.class)
@Import(CamundaConfiguration.class)
@Slf4j
public class CamundaAutoConfiguration {
	
	public CamundaAutoConfiguration() {
		log.info("[AutoConfiguration==>]:Camunda Module Loading");
		CommonConstant.addModule(new ModuleVO("Camunda","工作流模块"));
	}

}
