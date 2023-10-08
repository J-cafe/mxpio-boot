package com.mxpioframework.dingtalk.provider;


import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.mxpioframework.cache.provider.CacheProvider;
import com.mxpioframework.common.exception.MBootException;
import com.mxpioframework.common.util.SpringUtil;
import com.mxpioframework.security.Constants;
import com.taobao.api.ApiException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author : wpp
 * @Datetime : 2023/10/7 16:45
 * @Desc :
 * @Modor :  Modifytime:
 * @modDesc :
 */
@Data
@Component
@Slf4j
public class AppTokenFactory {
    @Value("${dingtalk.appKey}")
    private String appKey;

    @Value("${dingtalk.appSecret}")
    private String appSecret;

    public static final long cacheTime = 7199;
    private static long LAST_TIME = 0;
    private static String ACCESS_TOKEN = null;

    public String getAppToken() {
        CacheProvider cacheProvider = SpringUtil.getBean(CacheProvider.class);
        if(cacheProvider != null) {
            String accessToken = (String) cacheProvider.get(Constants.ThirdAccessTokenKeyEnum.DingTalk.getCode());
            if (StringUtils.isNotBlank(accessToken)){
                return accessToken;
            }
            OapiGettokenResponse fromDingTalk = this.getAppTokenFromDingTalk();
            cacheProvider.set(Constants.ThirdAccessTokenKeyEnum.DingTalk.getCode(),fromDingTalk.getAccessToken(),fromDingTalk.getExpiresIn());
            return fromDingTalk.getAccessToken();
        }else{
            long curTime = System.currentTimeMillis();
            long differ = curTime - LAST_TIME;

            if (ACCESS_TOKEN != null && differ < cacheTime)
                return ACCESS_TOKEN;

            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
            OapiGettokenRequest request = new OapiGettokenRequest();
            request.setAppkey(appKey);
            request.setAppsecret(appSecret);
            request.setHttpMethod("GET");
            OapiGettokenResponse response = null;
            try {
                response = client.execute(request);
            } catch (ApiException e) {
                log.error("dingUtil getAppToken error", e);
                throw new MBootException( "获取token失败");
            }
            if (!response.isSuccess()) {
                log.error("dingUtil getAccessTokenAfresh failed, errorCode={}, errorMsg={}", response.getErrcode(), response.getErrmsg());
                return null;
            }
            ACCESS_TOKEN = response.getAccessToken();
            LAST_TIME = curTime;

            return ACCESS_TOKEN;
        }
    }

    private OapiGettokenResponse getAppTokenFromDingTalk(){
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
        OapiGettokenRequest request = new OapiGettokenRequest();
        request.setAppkey(appKey);
        request.setAppsecret(appSecret);
        request.setHttpMethod("GET");
        OapiGettokenResponse response = null;
        try {
            response = client.execute(request);
        } catch (ApiException e) {
            log.error("dingUtil getAppToken error", e);
            throw new MBootException( "获取应用token失败:"+e.getErrMsg());
        }
        if (!response.isSuccess()) {
            log.error("dingUtil getAccessTokenAfresh failed, errorCode={}, errorMsg={}", response.getErrcode(), response.getErrmsg());
            throw new MBootException( "获取token失败:"+response.getErrmsg());
        }
        return response;
    }
}
