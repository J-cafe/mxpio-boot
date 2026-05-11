package com.mxpioframework.security.access.datascope.provider;

import com.mxpioframework.security.entity.User;
import com.mxpioframework.security.util.SecurityUtils;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Set;

@Data
public class DataScapeContext {

    private User user;

    private Set<String> deptCode;

    private Collection<? extends GrantedAuthority> authorities;

    private Set<String> authorityKeys;

    public static DataScapeContext initContext(){
        DataScapeContext context = new DataScapeContext();
        context.user = SecurityUtils.getLoginUser();
        context.deptCode = SecurityUtils.getDeptCode();
        context.authorities = SecurityUtils.getAuthorities();
        context.authorityKeys = SecurityUtils.getAuthorityKeys();
        return context;
    }
}
