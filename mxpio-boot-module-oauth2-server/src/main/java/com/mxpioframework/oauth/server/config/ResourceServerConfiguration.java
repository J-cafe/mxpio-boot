package com.mxpioframework.oauth.server.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/*@Configuration
@EnableResourceServer*/
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Value("${mxpio.systemAnonymous}")
    private String systemAnonymous;

    @Value("${mxpio.customAnonymous}")
    private String customAnonymous;


    @Override
    public void configure(HttpSecurity http) throws Exception {
        /*http.authorizeRequests()
                .antMatchers(mergeAnonymous()).permitAll()
                // 添加SWAGGER地址
                .antMatchers(Constants.SWAGGER_WHITELIST).permitAll()
                .antMatchers(Constants.MULTITENANT_WHITELIST).permitAll()
                .antMatchers(Constants.OAUTH_WHITELIST).permitAll()
                .anyRequest().authenticated();*/
        http.authorizeRequests().anyRequest().authenticated();
    }



    private String[] mergeAnonymous() {
        String[] anonymous = null;
        if (StringUtils.isNotBlank(systemAnonymous) && StringUtils.isNotBlank(customAnonymous)) {
            anonymous = (systemAnonymous + "," + customAnonymous).split(",");
        } else if (StringUtils.isNotBlank(systemAnonymous)) {
            anonymous = (systemAnonymous).split(",");
        } else if (StringUtils.isNotBlank(customAnonymous)) {
            anonymous = (customAnonymous).split(",");
        }
        return anonymous;
    }
}
