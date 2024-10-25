package com.mxpioframework.security.anthentication;
import org.springframework.security.core.AuthenticationException;

//三方登录异常
public class ThirdAuthorizeException extends AuthenticationException {
	private static final long serialVersionUID = 1L;

	public ThirdAuthorizeException(String msg) {
        super(msg);
    }

    public ThirdAuthorizeException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
