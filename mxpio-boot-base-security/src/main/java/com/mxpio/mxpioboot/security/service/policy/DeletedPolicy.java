package com.mxpio.mxpioboot.security.service.policy;

import java.lang.reflect.Field;

import com.mxpio.mxpioboot.jpa.policy.impl.AbstractGeneratorPolicy;
import com.mxpio.mxpioboot.jpa.policy.impl.CrudType;

public class DeletedPolicy extends AbstractGeneratorPolicy {

	@Override
	public CrudType getType() {
		return CrudType.SAVE;
	}

	@Override
	protected Object getValue(Object entity, Field field) {
		return false;
	}

}
