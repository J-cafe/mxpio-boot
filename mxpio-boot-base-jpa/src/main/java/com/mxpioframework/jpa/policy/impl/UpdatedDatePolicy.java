package com.mxpioframework.jpa.policy.impl;

import com.mxpioframework.common.util.BeanReflectionUtils;

import java.util.Date;

public class UpdatedDatePolicy extends AbstractGeneratorPolicy {

	@Override
	public CrudType getType() {
		return CrudType.UPDATE;
	}

	@Override
	protected Object getValue(Object entity, String field) {
        Object value = BeanReflectionUtils.getPropertyValue(entity, field);
        if ("".equals(value)) {
            value = null;
        }
        if (value == null) {
            return new Date();
        }
        return value;
	}

}
