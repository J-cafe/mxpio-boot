package com.mxpio.mxpioboot.jpa.policy.impl;

import java.lang.reflect.Field;
import java.util.Date;

public class UpdatedDatePolicy extends AbstractGeneratorPolicy {

	@Override
	public CrudType getType() {
		return CrudType.UPDATE;
	}

	@Override
	protected Object getValue(Object entity, Field field) {
		return new Date();
	}

}
