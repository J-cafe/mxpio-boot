package com.mxpioframework.oauth.server.service.impl;

import com.mxpioframework.common.exception.MBootException;
import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.oauth.server.entity.OauthClientDetails;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Arrays;

import java.util.stream.Collectors;

@Service("mxpio.oauth2.server.clientDetailService")
public class Oauth2ClientDetailsServiceImpl implements ClientDetailsService {


    @Override
    @Transactional
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {

        OauthClientDetails client = JpaUtil.getOne(OauthClientDetails.class,clientId);
        if(client==null){
            throw new MBootException("ClientId not exists");
        }
        if(StringUtils.isBlank(client.getGrantTypes())){
            throw new MBootException("GrantTypes is empty");
        }
        if(StringUtils.isBlank(client.getScope())){
            throw new MBootException("Scope is empty");
        }

        BaseClientDetails details = new BaseClientDetails();
        details.setClientId(client.getClientId());
        details.setClientSecret(client.getClientSecret());

        // 授权类型
        details.setAuthorizedGrantTypes(
                Arrays.asList(client.getGrantTypes().split(",")));

        // 作用域
        details.setScope(
                Arrays.asList(client.getScope().split(",")));

        // 资源ID
        if (StringUtils.isNotBlank(client.getResourceIds())) {
            details.setResourceIds(
                    Arrays.asList(client.getResourceIds().split(",")));
        }
        // 回调地址
        if (StringUtils.isNotBlank(client.getRedirectUri())) {
            details.setRegisteredRedirectUri(
                    Arrays.stream(client.getRedirectUri().split(","))
                            .collect(Collectors.toSet()));
        }
        // 权限
        if (StringUtils.isNotBlank(client.getAuthorities())) {
            details.setAuthorities(
                    AuthorityUtils.commaSeparatedStringToAuthorityList(
                            client.getAuthorities()));
        }
        // Token有效期
        details.setAccessTokenValiditySeconds(
                client.getAccessTokenValidity());
        details.setRefreshTokenValiditySeconds(
                client.getRefreshTokenValidity());

        // 自动批准的Scope
        if (StringUtils.isNotBlank(client.getAutoApproveScopes())) {
            details.setAutoApproveScopes(
                    Arrays.asList(client.getAutoApproveScopes().split(",")));
        }
        return details;
    }
}
