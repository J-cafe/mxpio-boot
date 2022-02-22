package com.mxpioframework.security.access.provider.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.core.annotation.Order;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.security.access.provider.GrantedAuthorityProvider;
import com.mxpioframework.security.entity.RoleGrantedAuthority;
import com.mxpioframework.security.entity.UserDept;

@Component
@Order(2)
public class DeptGrantedAuthorityProvider implements GrantedAuthorityProvider  {
	
	@Override
	public Collection<? extends GrantedAuthority> provide(UserDetails userDetails) {
		List<UserDept> userDepts = JpaUtil.linq(UserDept.class)
				.equal("userId", userDetails.getUsername())
				.list();
		List<GrantedAuthority> authorities = null;
		
		for(UserDept userDept : userDepts){
			if(authorities == null){
				authorities = new ArrayList<GrantedAuthority>();
			}
			authorities.addAll(JpaUtil.linq(RoleGrantedAuthority.class)
							.equal("actorId", userDept.getDeptId())
							.list());
		}
		
		return authorities;
	}
}
