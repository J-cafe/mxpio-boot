package com.mxpioframework.security.anthentication;
import org.springframework.security.core.AuthenticationException;
/**
 * @Author : wpp
 * @Datetime : 2023/10/11 16:24
 * @Desc :
 * @Modor :  Modifytime:
 * @modDesc :
 */
//三方登录异常
public class ThirdAuthorizeException extends AuthenticationException {
    public ThirdAuthorizeException(String msg) {
        super(msg);
    }

    public ThirdAuthorizeException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
