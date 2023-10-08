package com.mxpioframework.security.anthentication;

import com.mxpioframework.security.entity.User;

/**
 * @Author : wpp
 * @Datetime : 2023/10/7 15:06
 * @Desc :
 * @Modor :  Modifytime:
 * @modDesc :
 */
public interface ThirdAuthorizeUserProvider {
    User getUser(String authCode);//根据授权码获取到系统用户
    boolean support(ThirdAuthorizeToken thirdAuthorizeToken);//
}
