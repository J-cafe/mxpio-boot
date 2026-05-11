package com.mxpioframework.log;

import com.mzt.logapi.starter.annotation.EnableLogRecord;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 模块配置类
 */
@Configuration
@AutoConfigurationPackage
@ComponentScan
@EnableAsync
@EnableLogRecord(tenant="com.mxpio")
public class LogConfiguration {

}
