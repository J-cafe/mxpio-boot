package com.mxpioframework.security.util;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.mxpioframework.security.entity.User;

public class SecurityUtils {
	
	public static User getLoginUser() {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return user;
	}
	
	public static String getLoginUsername() {
		String username = getLoginUser().getUsername();
		return username;
	}
	
	public static Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		return authorities;
	}

}
