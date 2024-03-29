package com.mxpioframework.security.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.mxpioframework.security.entity.User;
import com.mxpioframework.security.service.DeptService;

public class SecurityUtils {
	
	public static User getLoginUser() {
		if (SecurityContextHolder.getContext().getAuthentication()==null){
			return null;
		}
		if ("anonymousUser".equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal())){//匿名用户直接返回null
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
	
	public static Set<String> getAuthorityKeys(){
		return getAuthorities().stream()
			     .map(r -> r.getAuthority()).collect(Collectors.toSet());
	}
}
