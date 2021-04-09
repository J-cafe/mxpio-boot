package com.mxpio.mxpioboot.jpa.policy.impl;

import java.util.Date;

public class CreatedDatePolicy extends AbstractGeneratorPolicy {

	@Override
	public CrudType getType() {
		return CrudType.SAVE;
	}

	@Override
	protected Object getValue(Object entity, String name) {
		return new Date();
	}

}
