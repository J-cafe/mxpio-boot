package com.mxpio.mxpioboot.jpa.policy;

import com.mxpio.mxpioboot.jpa.policy.impl.CrudType;

public interface GeneratorPolicy {
	void apply(Object entity, String name);
	
	CrudType getType();
}
