package com.mxpio.mxpioboot.autoconfigure.jpa;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.mxpio.mxpioboot.common.CommonConfiguration;
import com.mxpio.mxpioboot.jpa.LinqConfiguration;

import lombok.extern.slf4j.Slf4j;

@Configuration
@ConditionalOnClass(LinqConfiguration.class)
@AutoConfigureAfter(CommonConfiguration.class)
@Import(LinqConfiguration.class)
@Slf4j
public class LinqAutoConfiguration {
	
	public LinqAutoConfiguration() {
		log.info("[AutoConfiguration==>]:Linq Module Loading");
	}

}
