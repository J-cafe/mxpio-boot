package com.mxpioframework.security.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.mxpioframework.security.entity.User;
import com.mxpioframework.security.service.DeptService;

public class SecurityUtils {
	
	public static User getLoginUser() {
		if (SecurityContextHolder.getContext().getAuthentication()==null){
			return null;
		}
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return user;
	}
	
	public static String getLoginUsername() {
		if(getLoginUser()==null){
			return null;
		}
		return getLoginUser().getUsername();
	}
	
	public static Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		return authorities;
	}
	
	public static Set<String> getDeptCode(){
		DeptService deptService = ApplicationContextProvider.getBean(DeptService.class);
		Map<String,Set<String>> deptMap = deptService.getAllDeptCodeGroupByUser();
		return deptMap.get(getLoginUsername());
	}
}
