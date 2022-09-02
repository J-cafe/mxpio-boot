package com.mxpioframework.autoconfigure.filestorage;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.mxpioframework.autoconfigure.security.SecurityAutoConfiguration;
import com.mxpioframework.common.CommonConstant;
import com.mxpioframework.common.vo.ModuleVO;
import com.mxpioframework.filestorage.FilestorageConfiguration;

import lombok.extern.slf4j.Slf4j;

/**
 * 自动装载 文件模块 mxpio-boot-base-filestorage
 * @author MxpIO
 *
 */
@Configuration
@ConditionalOnClass(FilestorageConfiguration.class)
@AutoConfigureAfter(SecurityAutoConfiguration.class)
@Import(FilestorageConfiguration.class)
@Slf4j
public class FilestorageAutoConfiguration {
	
	public FilestorageAutoConfiguration() {
		log.info("[AutoConfiguration==>]:Filestorage Module Loading");
		CommonConstant.addModule(new ModuleVO("Filestorage","文件管理模块"));
	}

}
