package com.mxpio.mxpioboot.jpa.policy.impl;

import java.util.UUID;

import com.mxpio.mxpioboot.jpa.BeanReflectionUtils;

public class UUIDPolicy extends AbstractGeneratorPolicy {

	@Override
	protected Object getValue(Object entity, String name) {
		Object value = BeanReflectionUtils.getPropertyValue(entity, name);
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
