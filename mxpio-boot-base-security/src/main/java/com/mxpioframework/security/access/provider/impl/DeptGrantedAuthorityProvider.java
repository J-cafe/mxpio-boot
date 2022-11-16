package com.mxpioframework.security.access.provider.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.security.access.provider.GrantedAuthorityProvider;
import com.mxpioframework.security.entity.RoleGrantedAuthority;
import com.mxpioframework.security.service.DeptService;

@Component
@Order(2)
public class DeptGrantedAuthorityProvider implements GrantedAuthorityProvider  {
	
	@Autowired
	private DeptService deptService;
	
	@Override
	public Collection<? extends GrantedAuthority> provide(UserDetails userDetails) {
		Set<String> deptIds = deptService.getDeptKeysByUser(userDetails.getUsername(), "id");
		List<GrantedAuthority> authorities = null;
		
		for(String deptId : deptIds){
			if(authorities == null){
				authorities = new ArrayList<GrantedAuthority>();
			}
			List<RoleGrantedAuthority> actorId = JpaUtil.linq(RoleGrantedAuthority.class)
					.equal("actorId", deptId)
					.list();
			authorities.addAll(actorId);
		}
		
		return authorities;
	}
}
