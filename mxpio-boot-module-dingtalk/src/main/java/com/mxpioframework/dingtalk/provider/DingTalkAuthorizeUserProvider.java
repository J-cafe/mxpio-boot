package com.mxpioframework.dingtalk.provider;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiUserGetuserinfoRequest;
import com.dingtalk.api.response.OapiUserGetuserinfoResponse;
import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.security.anthentication.ThirdAuthorizeException;
import com.mxpioframework.security.anthentication.ThirdAuthorizeToken;
import com.mxpioframework.security.anthentication.ThirdAuthorizeUserProvider;
import com.mxpioframework.security.entity.User;
import com.taobao.api.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author : wpp
 * @Datetime : 2023/10/7 16:36
 * @Desc :
 * @Modor :  Modifytime:
 * @modDesc :
 */
@Component
public class DingTalkAuthorizeUserProvider implements ThirdAuthorizeUserProvider {
    @Autowired
    private AppTokenFactory appTokenFactory;

    @Override
    @Transactional
    public User getUser(String authCode) {

        String access_token= appTokenFactory.getAppToken();
        // 获取用户信息
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/getuserinfo");
        OapiUserGetuserinfoRequest request = new OapiUserGetuserinfoRequest();
        request.setCode(authCode);
        request.setHttpMethod("GET");
        OapiUserGetuserinfoResponse response;
        try {
            response = client.execute(request, access_token);
        } catch (ApiException e) {
            e.printStackTrace();
            throw new ThirdAuthorizeException( "获取钉钉用户UserId失败："+e.getMessage());
        }
        String userId = response.getUserid();
        List<User> user = JpaUtil.linq(User.class).equal("thirdId", userId).list();
        if (user==null||user.size()==0){
            String msg = "三方用户ID未能在应用系统中匹配到用户";
            if (StringUtils.isNotBlank(response.getErrmsg())){
                msg += "，原因："+response.getErrmsg();
            }else{
                msg += "，请检查用户管理第三方账号信息是否已绑定或者有效";
            }
            throw new ThirdAuthorizeException(msg);
        }
        return user.get(0);
    }

    @Override
    public boolean support(ThirdAuthorizeToken thirdAuthorizeToken) {
        return StringUtils.equals(thirdAuthorizeToken.getThirdPlatformType().toString(),"dingtalk");//钉钉
    }
}
