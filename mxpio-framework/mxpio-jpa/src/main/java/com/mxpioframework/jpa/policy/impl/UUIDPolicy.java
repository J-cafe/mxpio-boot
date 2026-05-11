package com.mxpioframework.jpa.policy.impl;

import java.util.UUID;

import com.mxpioframework.common.util.BeanReflectionUtils;

public class UUIDPolicy extends AbstractGeneratorPolicy {

	@Override
	protected Object getValue(Object entity, String name) {
		Object value = BeanReflectionUtils.getPropertyValue(entity, name);
		if ("".equals(value)) {
			value = null;
		}
		if (value == null) {
			return UUID.randomUUID().toString();
		}
		return value;
	}

	@Override
	public CrudType getType() {
		return CrudType.SAVE;
	}

}
