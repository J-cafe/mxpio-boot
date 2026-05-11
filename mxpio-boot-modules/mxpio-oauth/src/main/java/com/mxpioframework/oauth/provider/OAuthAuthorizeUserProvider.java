package com.mxpioframework.oauth.provider;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.security.Constants;
import com.mxpioframework.security.anthentication.ThirdAuthorizeException;
import com.mxpioframework.security.anthentication.ThirdAuthorizeToken;
import com.mxpioframework.security.anthentication.ThirdAuthorizeUserProvider;
import com.mxpioframework.security.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 三方授权用户获取
 */
@Component
public class OAuthAuthorizeUserProvider implements ThirdAuthorizeUserProvider {
    @Value("${spring.security.oauth2.client.registration.authing.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.authing.client-secret}")
    private String clientSecret;
    @Value("${spring.security.oauth2.client.registration.authing.redirect-uri}")
    private String redirectUri;
    @Value("${spring.security.oauth2.client.provider.authing.issuer-uri}")
    private String issueUri;

    @Override
    @Transactional
    public User getUser(String authCode) {
        //根据授权码获取token
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("code",authCode);
        paramMap.put("client_id",clientId);
        paramMap.put("client_secret",clientSecret);
        paramMap.put("grant_type","authorization_code");
        paramMap.put("redirect_uri",redirectUri);
        String result = HttpUtil.post(issueUri+"/token", paramMap);

        JSONObject jsonObject = JSONObject.parseObject(result);
        if (jsonObject.containsKey("error")){
            throw new ThirdAuthorizeException( "获取auth2 Token失败："+jsonObject.getString("error_description"));
        }

        //根据返回的token获取sub，及三方用户的id
        String access_token = jsonObject.getString("access_token");
        HashMap<String, String> headers = new HashMap<>();//存放请求头，可以存放多个请求头
        headers.put("Authorization", access_token);
        //发送get请求并接收响应数据
        String me = HttpUtil.createGet(issueUri+"/me").addHeaders(headers).execute().body();
        JSONObject tokenInfo =  JSONObject.parseObject(me);
        if (tokenInfo.containsKey("error")){
            throw new ThirdAuthorizeException( "验证auth2 Token失败："+tokenInfo.getString("error_description"));
        }
        //根据三方id获取对应本系统的用户
        String sub = tokenInfo.getString("sub");
        List<User> user = JpaUtil.linq(User.class).equal("thirdId", sub).list();
        if (user==null|| user.isEmpty()){
            String msg = "三方用户ID未能在应用系统中匹配到用户，请检查用户管理第三方账号信息是否已绑定或者有效";
            throw new ThirdAuthorizeException(msg);
        }
        return user.get(0);
    }

    @Override
    public boolean support(ThirdAuthorizeToken thirdAuthorizeToken) {
        return StringUtils.equals(thirdAuthorizeToken.getThirdPlatformType().toString(), Constants.ThirdPlatformTypeEnum.OAuth.getCode());//oauth
    }
}