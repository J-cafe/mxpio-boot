package com.mxpioframework.security.processor;

import com.mxpioframework.security.entity.Url;

public interface UrlProcessor {
	
	boolean preProcess(Context<Url> context);

	void postProcess(Context<Url> context);
	
}
