package com.mxpioframework.autoconfigure.module.datav;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.mxpioframework.autoconfigure.security.SecurityAutoConfiguration;
import com.mxpioframework.common.CommonConstant;
import com.mxpioframework.common.vo.ModuleVO;
import com.mxpioframework.module.datav.DatavConfiguration;

import lombok.extern.slf4j.Slf4j;

/**
 * 自动装载 大屏模块 mxpio-boot-module-datav
 * @author MxpIO
 *
 */
@Configuration
@ConditionalOnClass(DatavConfiguration.class)
@AutoConfigureAfter(SecurityAutoConfiguration.class)
@Import(DatavConfiguration.class)
@Slf4j
public class DatavAutoConfiguration {

	public DatavAutoConfiguration() {
		//log.info("[AutoConfiguration==>]:Datav Module Loading");
		//CommonConstant.addModule(new ModuleVO("Datav","大屏模块"));
	}

}
