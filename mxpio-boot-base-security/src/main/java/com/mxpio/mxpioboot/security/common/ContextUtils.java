package com.mxpio.mxpioboot.security.common;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.mxpio.mxpioboot.security.entity.User;

public class ContextUtils {

	public static User getLoginUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof User) {
			return (User) authentication.getPrincipal();
			
		}
		return null;
	}
	
	public static String getLoginUsername() {
		User user = getLoginUser();
		if (user != null) {
			return user.getUsername();
		}
		return null;
	}
}
