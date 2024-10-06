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
	public void environmentPrepared(ConfigurableBootstrapContext bootstrapContext,
									ConfigurableEnvironment environment) {

	}

    @Override
	public void starting(ConfigurableBootstrapContext bootstrapContext) {
		Properties properties = new Properties();
		application.setDefaultProperties(properties);
	}

	@Override
	public void started(ConfigurableApplicationContext context) {
		// TODO Auto-generated method stub

	}

	@Override
	public void running(ConfigurableApplicationContext context) {
		// TODO Auto-generated method stub

	}

	@Override
	public void failed(ConfigurableApplicationContext context, Throwable exception) {
		// TODO Auto-generated method stub

	}

}
