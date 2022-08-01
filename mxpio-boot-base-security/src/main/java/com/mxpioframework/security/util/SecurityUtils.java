package com.mxpioframework.security.util;

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

}
