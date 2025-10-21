package com.mxpioframework.jpa.policy.impl;

import com.mxpioframework.common.util.BeanReflectionUtils;

import java.util.Date;

public class CreatedDatePolicy extends AbstractGeneratorPolicy {

	@Override
	public CrudType getType() {
		return CrudType.SAVE;
	}

	@Override
	protected Object getValue(Object entity, String name) {
        Object value = BeanReflectionUtils.getPropertyValue(entity, name);
        if ("".equals(value)) {
            value = null;
        }
        if (value == null) {
            return new Date();
        }
        return value;
	}

}
