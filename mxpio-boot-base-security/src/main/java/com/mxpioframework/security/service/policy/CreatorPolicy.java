package com.mxpioframework.security.service.policy;

import com.mxpioframework.jpa.policy.impl.AbstractGeneratorPolicy;
import com.mxpioframework.jpa.policy.impl.CrudType;
import com.mxpioframework.security.util.SecurityUtils;

public class CreatorPolicy extends AbstractGeneratorPolicy {

	@Override
	public CrudType getType() {
		return CrudType.SAVE;
	}

	@Override
	protected Object getValue(Object entity, String name) {
		return SecurityUtils.getLoginUsername();
	}

}
