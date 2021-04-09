package com.mxpio.mxpioboot.security.service.policy;

import com.mxpio.mxpioboot.jpa.policy.impl.AbstractGeneratorPolicy;
import com.mxpio.mxpioboot.jpa.policy.impl.CrudType;

public class DeletedPolicy extends AbstractGeneratorPolicy {

	@Override
	public CrudType getType() {
		return CrudType.SAVE;
	}

	@Override
	protected Object getValue(Object entity, String name) {
		return false;
	}

}
