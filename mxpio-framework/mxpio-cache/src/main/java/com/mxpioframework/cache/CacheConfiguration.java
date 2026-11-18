package com.mxpioframework.cache;

import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 模块配置类
 */
@Configuration
@AutoConfigurationPackage
@ComponentScan
@EnableConfigurationProperties(CacheProperties.class)
public class CacheConfiguration {

}
