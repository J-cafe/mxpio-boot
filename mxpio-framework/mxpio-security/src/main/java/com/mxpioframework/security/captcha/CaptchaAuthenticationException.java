package com.mxpioframework.security.captcha;

import org.springframework.security.core.AuthenticationException;

public class CaptchaAuthenticationException extends AuthenticationException {

	private static final long serialVersionUID = 1L;

	public CaptchaAuthenticationException(String msg) {
		super(msg);
	}

}
