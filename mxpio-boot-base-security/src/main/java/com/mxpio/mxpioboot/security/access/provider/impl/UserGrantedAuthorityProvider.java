package com.mxpio.mxpioboot.security.access.provider.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.core.annotation.Order;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.mxpio.mxpioboot.jpa.JpaUtil;
import com.mxpio.mxpioboot.security.access.provider.GrantedAuthorityProvider;
import com.mxpio.mxpioboot.security.entity.RoleGrantedAuthority;

/**

 * 默认用户授权信息提供者

 * @author Kevin Yang (mailto:kevin.yang@bstek.com)

 * @since 2016年2月27日

 */
@Component
@Order(1)
public class UserGrantedAuthorityProvider implements GrantedAuthorityProvider {

	@Override
	public Collection<? extends GrantedAuthority> provide(
			UserDetails userDetails) {
		List<GrantedAuthority> authorities = JpaUtil.linq(RoleGrantedAuthority.class).equal("actorId", userDetails.getUsername()).list();
		return authorities;
	}

}
