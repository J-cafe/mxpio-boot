package com.mxpio.mxpioboot.security.service.policy;

import java.lang.reflect.Field;

import com.mxpio.mxpioboot.jpa.policy.impl.AbstractGeneratorPolicy;
import com.mxpio.mxpioboot.jpa.policy.impl.CrudType;
import com.mxpio.mxpioboot.security.common.ContextUtils;

public class CreatorPolicy extends AbstractGeneratorPolicy {

	@Override
	public CrudType getType() {
		return CrudType.SAVE;
	}

	@Override
	protected Object getValue(Object entity, Field field) {
		return ContextUtils.getLoginUsername();
	}

}
