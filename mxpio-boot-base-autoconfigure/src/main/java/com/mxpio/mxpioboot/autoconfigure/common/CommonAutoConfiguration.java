package com.mxpio.mxpioboot.autoconfigure.common;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.mxpio.mxpioboot.common.CommonConfiguration;

import lombok.extern.slf4j.Slf4j;

@Configuration
@ConditionalOnClass(CommonConfiguration.class)
@Import(CommonConfiguration.class)
@Slf4j
public class CommonAutoConfiguration {
	
	public CommonAutoConfiguration() {
		log.info("[AutoConfiguration==>]:Common Module Loading");
	}

}
