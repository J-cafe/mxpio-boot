package com.mxpioframework.multitenant.policy;

import com.mxpioframework.jpa.policy.impl.AbstractGeneratorPolicy;
import com.mxpioframework.jpa.policy.impl.CrudType;
import com.mxpioframework.multitenant.MultitenantUtils;

public class OrgIdPolicy extends AbstractGeneratorPolicy {

	@Override
	public CrudType getType() {
		return CrudType.SAVE;
	}

	@Override
	protected Object getValue(Object entity, String field) {
		return MultitenantUtils.getLoginOrgId();
	}

}
