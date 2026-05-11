package com.mxpioframework.security.processor;

import com.mxpioframework.security.entity.User;

public interface UserProcessor {
	
	boolean preProcess(Context<User> context);

	void postProcess(Context<User> context);

}
