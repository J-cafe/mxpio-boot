package com.mxpioframework.autoconfigure.security;

import java.lang.reflect.Method;

import org.springframework.cache.interceptor.KeyGenerator;

public class SecurityKeyGenerator implements KeyGenerator {

	@Override
	public Object generate(Object target, Method method, Object... params) {
		return "MXPIO_SECURITY";
	}

}
