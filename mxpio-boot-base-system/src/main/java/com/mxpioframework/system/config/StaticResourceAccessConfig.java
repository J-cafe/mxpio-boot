package com.mxpioframework.system.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourceAccessConfig implements WebMvcConfigurer {
    @Value("${mxpio.systemResourceLocation}")
    String resFilePath;
    @Value("${mxpio.systemResourceMappingPath}")
    String resMappingPath;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(resMappingPath +"**").
                addResourceLocations("file:"+ resFilePath);
    }
}
