package com.mxpioframework.oauth.server.service.impl;

import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.oauth.server.entity.OauthClientDetails;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class Oauth2ClientDetailsServiceImpl implements ClientDetailsService {


    @Override
    @Transactional
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        return JpaUtil.getOne(OauthClientDetails.class,clientId);
    }
}
