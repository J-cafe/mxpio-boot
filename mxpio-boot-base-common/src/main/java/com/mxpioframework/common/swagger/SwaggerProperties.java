package com.mxpioframework.common.swagger;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "mxpio.swagger")
@Data
public class SwaggerProperties {
	
	private String title;
	
	private String desc;
	
	private String version;
	
	private String author;
	
	private String email;
	
	private String homepage;
	
	private String license;
	
	private String licenseUrl;

}
