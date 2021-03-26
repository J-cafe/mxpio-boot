package com.mxpio.mxpioboot.jpa.initiator.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.mxpio.mxpioboot.jpa.JpaUtil;
import com.mxpio.mxpioboot.jpa.strategy.GetEntityManagerFactoryStrategy;

/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2017年7月3日
 */
@Component
public class JpaUtilInitiator implements com.mxpio.mxpioboot.jpa.initiator.JpaUtilInitiator {

	@Autowired
	private GetEntityManagerFactoryStrategy getEntityManagerFactoryStrategy;

	@Override
	public void initialize(ApplicationContext applicationContext) {
		JpaUtil.setGetEntityManagerFactoryStrategy(getEntityManagerFactoryStrategy);
		JpaUtil.setApplicationContext(applicationContext);
	}

}
