package com.mxpioframework.security.kaptcha;

import org.springframework.security.core.AuthenticationException;

public class KaptchaAuthenticationException extends AuthenticationException {

	private static final long serialVersionUID = 1L;

	public KaptchaAuthenticationException(String msg) {
		super(msg);
	}

}
