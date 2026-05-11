package com.mxpioframework.security.service;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 主要提供用户的授权信息的收集功能
 */
public interface GrantedAuthorityService {

	/**

	 * 获取用户的授权信息

	 * @param userDetails 用户信息

	 * @return 权限信息

	 */
	Collection<? extends GrantedAuthority> getGrantedAuthorities(UserDetails userDetails);

}
