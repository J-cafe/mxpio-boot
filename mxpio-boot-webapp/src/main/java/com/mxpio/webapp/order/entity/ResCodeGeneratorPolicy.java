package com.mxpio.webapp.order.entity;

import java.util.UUID;

import com.mxpioframework.common.util.BeanReflectionUtils;
import com.mxpioframework.jpa.policy.impl.AbstractGeneratorPolicy;
import com.mxpioframework.jpa.policy.impl.CrudType;

public class ResCodeGeneratorPolicy extends AbstractGeneratorPolicy {

	@Override
	public CrudType getType() {
		return CrudType.SAVE;
	}

	@Override
	protected Object getValue(Object entity, String field) {
		Object value = BeanReflectionUtils.getPropertyValue(entity, field);
		if ("".equals(value)) {
			value = null;
		}
		if (value == null) {
			return UUID.randomUUID().toString();
		}
		return value;
	}

}
