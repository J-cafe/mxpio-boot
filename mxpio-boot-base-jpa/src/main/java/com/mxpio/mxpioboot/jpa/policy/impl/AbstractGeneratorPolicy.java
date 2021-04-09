package com.mxpio.mxpioboot.jpa.policy.impl;

import com.mxpio.mxpioboot.jpa.BeanReflectionUtils;
import com.mxpio.mxpioboot.jpa.policy.GeneratorPolicy;

public abstract class AbstractGeneratorPolicy implements GeneratorPolicy {

	@Override
	public void apply(Object entity, String name) {
		BeanReflectionUtils.setPropertyValue(entity, name, getValue(entity, name));
	}
	
	protected abstract Object getValue(Object entity, String field);

}
