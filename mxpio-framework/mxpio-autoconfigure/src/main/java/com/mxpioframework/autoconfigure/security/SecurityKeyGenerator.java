package com.mxpioframework.autoconfigure.security;

import java.lang.reflect.Method;

import org.springframework.cache.interceptor.KeyGenerator;

public class SecurityKeyGenerator implements KeyGenerator {

	private final String keyPrefix;

	public SecurityKeyGenerator(String keyPrefix) {
		this.keyPrefix = keyPrefix;
	}

	@Override
	public Object generate(Object target, Method method, Object... params) {
		return keyPrefix + "SECURITY";
	}

}
