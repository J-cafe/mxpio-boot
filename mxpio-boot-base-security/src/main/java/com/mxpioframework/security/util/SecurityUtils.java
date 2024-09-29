package com.mxpioframework.security.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.mxpioframework.security.access.datascope.provider.DataScapeProvider;
import com.mxpioframework.security.service.RbacCacheService;
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
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
	public static String getLoginUsername() {
		if(getLoginUser()==null){
			return null;
		}
		return getLoginUser().getUsername();
	}
	
	public static Collection<? extends GrantedAuthority> getAuthorities() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities();
	}
	
	public static Set<String> getDeptCode(){
		RbacCacheService rbacCacheService = ApplicationContextProvider.getBean(RbacCacheService.class);
		Map<String,Set<String>> deptMap = rbacCacheService.getAllDeptCodeGroupByUser();
		return deptMap.get(getLoginUsername());
	}
	
	public static Set<String> getAuthorityKeys(){
		return getAuthorities().stream()
			     .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
	}

	public static Map<String, DataScapeProvider> getDataScapeProviderMap(){
		return ApplicationContextProvider.getApplicationContextSpring().getBeansOfType(DataScapeProvider.class);
	}
}
