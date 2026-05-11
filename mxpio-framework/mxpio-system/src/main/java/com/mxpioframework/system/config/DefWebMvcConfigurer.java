package com.mxpioframework.system.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class DefWebMvcConfigurer implements WebMvcConfigurer {

	@Autowired
	private HandlerMethodArgumentResolver criteriaHandlerMethodArgumentResolver;
	
	
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(criteriaHandlerMethodArgumentResolver);
		WebMvcConfigurer.super.addArgumentResolvers(resolvers);
	}
}
