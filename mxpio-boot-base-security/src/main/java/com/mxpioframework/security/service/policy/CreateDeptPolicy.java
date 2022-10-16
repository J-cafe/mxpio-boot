package com.mxpioframework.security.service.policy;

import java.util.Set;

import com.mxpioframework.jpa.policy.impl.AbstractGeneratorPolicy;
import com.mxpioframework.jpa.policy.impl.CrudType;
import com.mxpioframework.security.util.SecurityUtils;

public class CreateDeptPolicy extends AbstractGeneratorPolicy {

	@Override
	public CrudType getType() {
		return CrudType.SAVE;
	}

	@Override
	protected Object getValue(Object entity, String name) {
		Set<String> deptCodes = SecurityUtils.getDeptCode();
		return deptCodes==null?"":deptCodes.toArray(new String[0])[0];
	}

}
