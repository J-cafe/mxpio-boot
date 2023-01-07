package com.mxpioframework.security.processor;

import com.mxpioframework.security.entity.Dept;

public interface DeptProcessor {
	
	boolean preProcess(Context<Dept> context);

	void postProcess(Context<Dept> context);
	
}
