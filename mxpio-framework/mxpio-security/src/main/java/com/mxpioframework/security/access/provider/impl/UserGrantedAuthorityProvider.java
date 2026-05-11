package com.mxpioframework.security.access.provider.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.core.annotation.Order;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.security.access.provider.GrantedAuthorityProvider;
import com.mxpioframework.security.entity.RoleGrantedAuthority;

/**
 * 默认用户授权信息提供者
 */
@Component
@Order(1)
public class UserGrantedAuthorityProvider implements GrantedAuthorityProvider {

	@Override
	public Collection<? extends GrantedAuthority> provide(
			UserDetails userDetails) {
        return JpaUtil.linq(RoleGrantedAuthority.class).equal("actorId", userDetails.getUsername()).list();
	}

}
