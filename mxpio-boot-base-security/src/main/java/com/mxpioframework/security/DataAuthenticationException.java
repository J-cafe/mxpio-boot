package com.mxpioframework.security;

import org.springframework.security.core.AuthenticationException;

public class DataAuthenticationException extends AuthenticationException {

	private static final long serialVersionUID = 1L;

	public DataAuthenticationException(String msg) {
		super(msg);
	}

}
