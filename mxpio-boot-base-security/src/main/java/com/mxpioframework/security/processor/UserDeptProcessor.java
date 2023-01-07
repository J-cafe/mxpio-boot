package com.mxpioframework.security.processor;

import com.mxpioframework.security.entity.UserDept;

public interface UserDeptProcessor {
	
	boolean preProcess(Context<UserDept> context);

	void postProcess(Context<UserDept> context);
}
