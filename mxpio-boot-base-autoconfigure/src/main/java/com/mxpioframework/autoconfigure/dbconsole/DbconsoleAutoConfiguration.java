package com.mxpioframework.autoconfigure.dbconsole;

import javax.annotation.Resource;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

import com.mxpioframework.autoconfigure.security.SecurityAutoConfiguration;
import com.mxpioframework.common.CommonConstant;
import com.mxpioframework.common.vo.ModuleVO;
import com.mxpioframework.dbconsole.DbconsoleConfiguration;

import lombok.extern.slf4j.Slf4j;

/**
 * 自动装载 系统模块 mxpio-boot-base-dbconsole
 * @author MxpIO
 *
 */
@Configuration
@ConditionalOnClass(DbconsoleConfiguration.class)
@AutoConfigureAfter(SecurityAutoConfiguration.class)
@Import(DbconsoleConfiguration.class)
@Slf4j
public class DbconsoleAutoConfiguration {
	
	@Resource
	private Environment environment;
	
	public DbconsoleAutoConfiguration() {
		log.info("[AutoConfiguration==>]:Dbconsole Module Loading");
		CommonConstant.addModule(new ModuleVO("Dbconsole","云数据库模块"));
	}

}
