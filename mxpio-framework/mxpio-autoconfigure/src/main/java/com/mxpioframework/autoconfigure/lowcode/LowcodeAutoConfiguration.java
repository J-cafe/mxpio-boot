package com.mxpioframework.autoconfigure.lowcode;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.mxpioframework.autoconfigure.security.SecurityAutoConfiguration;
import com.mxpioframework.common.CommonConstant;
import com.mxpioframework.common.vo.ModuleVO;
import com.mxpioframework.lowcode.LowcodeConfiguration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@ConditionalOnClass(LowcodeConfiguration.class)
@AutoConfigureAfter(SecurityAutoConfiguration.class)
@Import(LowcodeConfiguration.class)
public class LowcodeAutoConfiguration {

	public LowcodeAutoConfiguration() {
		log.info("[AutoConfiguration==>]:Lowcode Module Loading");
		CommonConstant.addModule(new ModuleVO("Lowcode", "低代码模块"));
	}
}
