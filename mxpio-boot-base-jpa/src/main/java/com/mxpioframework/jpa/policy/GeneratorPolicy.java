package com.mxpioframework.jpa.policy;

import com.mxpioframework.jpa.policy.impl.CrudType;

public interface GeneratorPolicy {
	void apply(Object entity, String name);
	
	CrudType getType();
}
