package com.mxpioframework.autoconfigure;

import java.util.Properties;

import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

public class MxpioPreloadSpringApplicationRunListener implements SpringApplicationRunListener {

	private SpringApplication application;

	public MxpioPreloadSpringApplicationRunListener(SpringApplication application, String[] args) {
		this.application = application;
	}

	@Override
	public void contextPrepared(ConfigurableApplicationContext context) {

	}

	@Override
	public void contextLoaded(ConfigurableApplicationContext context) {

	}

	@Override
	public void starting(ConfigurableBootstrapContext bootstrapContext) {
		Properties properties = new Properties();
		//String basePackage = application.getClass().getPackage().getName();
		//String projectName = StringUtils.substringAfterLast(basePackage, ".");
		//properties.put("candoo.projectName", projectName);
		//properties.put("candoo.basePackage", basePackage);
		//properties.put("spring.mvc.staticPathPattern", "static/**");
		application.setDefaultProperties(properties);

	}

}
