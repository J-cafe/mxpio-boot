package com.mxpioframework.security.processor;

import com.mxpioframework.security.entity.Role;

public interface RoleProcessor {
	
	boolean preProcess(Context<Role> context);

	void postProcess(Context<Role> context);

}
