package com.mxpio.mxpioboot.jpa.policy.impl;

import java.lang.reflect.Field;
import java.util.UUID;

import org.springframework.util.ReflectionUtils;

public class UUIDPolicy extends AbstractGeneratorPolicy {

	@Override
	protected Object getValue(Object entity, Field field) {
		Object value = ReflectionUtils.getField(field, entity);
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
