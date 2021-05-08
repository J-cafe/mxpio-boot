package com.mxpio.mxpioboot.autoconfigure.ddm;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.mxpio.mxpioboot.common.CommonConfiguration;
import com.mxpio.mxpioboot.ddm.DdmConfiguration;
import lombok.extern.slf4j.Slf4j;

@Configuration
@ConditionalOnClass(DdmConfiguration.class)
@AutoConfigureAfter(CommonConfiguration.class)
@Import(DdmConfiguration.class)
@Slf4j
public class DdmAutoConfiguration {
	
	public DdmAutoConfiguration() {
		log.info("[AutoConfiguration==>]:Ddm Module Loading");
	}

}
