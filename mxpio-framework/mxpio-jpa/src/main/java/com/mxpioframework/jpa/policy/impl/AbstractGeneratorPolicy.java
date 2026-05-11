package com.mxpioframework.jpa.policy.impl;

import com.mxpioframework.common.util.BeanReflectionUtils;
import com.mxpioframework.jpa.policy.GeneratorPolicy;

public abstract class AbstractGeneratorPolicy implements GeneratorPolicy {

	@Override
	public void apply(Object entity, String name) {
		BeanReflectionUtils.setPropertyValue(entity, name, getValue(entity, name));
	}

	protected abstract Object getValue(Object entity, String field);

}
