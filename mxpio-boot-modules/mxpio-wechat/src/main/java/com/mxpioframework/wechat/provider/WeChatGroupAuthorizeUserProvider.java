package com.mxpioframework.wechat.provider;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.security.Constants;
import com.mxpioframework.security.anthentication.ThirdAuthorizeException;
import com.mxpioframework.security.anthentication.ThirdAuthorizeToken;
import com.mxpioframework.security.anthentication.ThirdAuthorizeUserProvider;
import com.mxpioframework.security.entity.User;
import com.mxpioframework.security.service.UserService;
import com.mxpioframework.wechat.cp.config.single.WxCpConfiguration;
import com.mxpioframework.wechat.cp.config.single.WxCpProperties;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpOAuth2Service;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.WxCpOauth2UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * @Author : wpp
 * @Desc :
 * @Modor :  Modifytime:
 * @modDesc : 企业微信小程序登录认证
 */
@Component
@Slf4j
public class WeChatGroupAuthorizeUserProvider implements ThirdAuthorizeUserProvider {
    @Value("${wechat.cp.appConfigs[0].agentId}")
    private Integer agentId;
    @Override
    @Transactional
    public User getUser(String authCode) {
        log.info("authCode:"+authCode);
        log.info("agentId:"+agentId);
        final WxCpService wxCpService = WxCpConfiguration.getCpService(agentId);
        if (wxCpService == null) {
            throw new IllegalArgumentException(String.format("未找到对应agentId=[%d]的配置，请核实！", agentId));
        }
        WxCpOAuth2Service oauth2Service = wxCpService.getOauth2Service();
        try {
            String accessToken = wxCpService.getAccessToken();
            log.info("accessToken:"+accessToken);
            WxCpOauth2UserInfo authUserInfo = oauth2Service.getUserInfo(authCode);
            log.info("authUserInfo信息：openId:{},userId:{}",authUserInfo.getOpenId(),authUserInfo.getUserId());
            //根据三方id获取对应本系统的用户
            String sub = authUserInfo.getUserId();
            List<User> user = JpaUtil.linq(User.class).equal("thirdId", sub).list();
            if (user==null|| user.isEmpty()){
                String msg = "三方用户ID未能在应用系统中匹配到用户，请检查用户管理第三方账号信息是否已绑定或者有效";
                throw new ThirdAuthorizeException(msg);
            }
            return user.get(0);
        } catch (WxErrorException e) {
            log.error("获取authUserInfo失败", e.getMessage());
            throw new RuntimeException("获取authUserInfo失败");
        }
    }

    @Override
    public boolean support(ThirdAuthorizeToken thirdAuthorizeToken) {
        return StringUtils.equals(thirdAuthorizeToken.getThirdPlatformType().toString(), Constants.ThirdPlatformTypeEnum.WeChatCp.getCode());//wechat
    }
}
