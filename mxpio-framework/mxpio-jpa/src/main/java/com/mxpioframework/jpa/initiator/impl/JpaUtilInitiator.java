package com.mxpioframework.jpa.initiator.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.jpa.policy.CriteriaPolicy;
import com.mxpioframework.jpa.strategy.GetEntityManagerFactoryStrategy;

@Component
public class JpaUtilInitiator implements com.mxpioframework.jpa.initiator.JpaUtilInitiator {

	@Autowired
	private GetEntityManagerFactoryStrategy getEntityManagerFactoryStrategy;

	@Autowired
	private CriteriaPolicy defaultQBCCriteriaPolicy;

	@Override
	public void initialize(ApplicationContext applicationContext) {
		JpaUtil.setGetEntityManagerFactoryStrategy(getEntityManagerFactoryStrategy);
		JpaUtil.setApplicationContext(applicationContext);
		JpaUtil.setDefaultQBCCriteriaPolicy(defaultQBCCriteriaPolicy);
	}

}
