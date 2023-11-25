package com.mxpioframework.msal.provider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.microsoft.aad.msal4j.AuthorizationCodeParameters;
import com.microsoft.aad.msal4j.ConfidentialClientApplication;
import com.microsoft.aad.msal4j.IAccount;
import com.microsoft.aad.msal4j.IAuthenticationResult;
import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.msal.utils.AuthHelper;
import com.mxpioframework.msal.utils.HttpClientHelper;
import com.mxpioframework.security.Constants;
import com.mxpioframework.security.anthentication.ThirdAuthorizeException;
import com.mxpioframework.security.anthentication.ThirdAuthorizeToken;
import com.mxpioframework.security.anthentication.ThirdAuthorizeUserProvider;
import com.mxpioframework.security.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.naming.ServiceUnavailableException;
import java.io.IOException;
import java.net.*;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @Author : wpp
 * @Datetime : 2023/10/30 13:57
 * @Desc :
 * @Modor :  Modifytime:
 * @modDesc :
 */
@Component
public class MSALAuthorizeUserProvider implements ThirdAuthorizeUserProvider {

     @Resource
     private AuthHelper authHelper;
    @Override
    @Transactional
    public User getUser(String authCode) {
        /*//前端调用api，直接返回用户信息
        if(StringUtils.isBlank(authCode)){
            throw new ThirdAuthorizeException("authCode传参为空，请检查接口数据！");
        }

        String[] split = authCode.split("@");
        String userId = split[0];
        List<User> user = JpaUtil.linq(User.class).equal("thirdId", userId).list();
        if (user==null||user.size()==0){
            String msg = "三方用户ID未能在应用系统中匹配到用户，请检查用户管理第三方账号信息是否已绑定或者有效";
            throw new ThirdAuthorizeException(msg);
        }
        return user.get(0);*/
        //后端方式获取用户信息
        IAuthenticationResult result;
        String currentUri = authHelper.getRedirectUriSignIn();
        //System.out.println("跳转地址："+authHelper.getRedirectUriSignIn());
        //System.out.println("authCode:"+authCode);
        try {
            ConfidentialClientApplication app = authHelper.createClientApplication();
            AuthorizationCodeParameters parameters = AuthorizationCodeParameters.builder(
                            authCode,
                            new URI(currentUri)).
                    build();
            Future<IAuthenticationResult> future = app.acquireToken(parameters);
            result = future.get();
        } catch (MalformedURLException e) {
            throw new ThirdAuthorizeException(e.getMessage());
        } catch (URISyntaxException e) {
            throw new ThirdAuthorizeException(e.getMessage());
        } catch (ExecutionException e) {
            throw new ThirdAuthorizeException(e.getMessage());
        } catch (InterruptedException e) {
            throw new ThirdAuthorizeException(e.getMessage());
        }
        if (result == null) {
            throw new ThirdAuthorizeException("authentication result was null");
        }

        //String s = result.accessToken();
        //System.out.println("result:"+result.toString());
        //System.out.println("result.account:"+result.account());
        //System.out.println("result.account.username:"+result.account().username());
        String userId = result.account().username();
        //System.out.println("userId:"+userId);
        if (userId.contains("@")){
            String[] split = userId.split("@");
            userId = split[0];
        }
        //System.out.println("thirdId:"+userId);
        List<User> user = JpaUtil.linq(User.class).equal("thirdId", userId).list();
        if (user==null||user.size()==0){
            String msg = "三方用户ID未能在应用系统中匹配到用户，请检查用户管理第三方账号信息是否已绑定或者有效";
            throw new ThirdAuthorizeException(msg);
        }
        return user.get(0);
    }

    @Override
    public boolean support(ThirdAuthorizeToken thirdAuthorizeToken) {
        return StringUtils.equals(thirdAuthorizeToken.getThirdPlatformType().toString(), Constants.ThirdPlatformTypeEnum.MSAL.getCode());//MSAL
    }
}
