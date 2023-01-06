package com.mxpioframework.security.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.security.cache.SecurityCacheEvict;
import com.mxpioframework.security.entity.Permission;
import com.mxpioframework.security.entity.RoleGrantedAuthority;
import com.mxpioframework.security.entity.User;
import com.mxpioframework.security.service.RoleUrlService;

@Service("mxpio.security.roleUrlService")
public class RoleUrlServiceImpl implements RoleUrlService {

	@Override
	@Transactional(readOnly = true)
	public List<Permission> load(String roleId) {
		List<Permission> result = new ArrayList<>();
		try{
			Set<String> roleIds = new HashSet<>();
			if(roleId == null) {
				User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				List<RoleGrantedAuthority> roleAuths = JpaUtil.linq(RoleGrantedAuthority.class).equal("actorId", user.getUsername()).list();
				roleIds = roleAuths.stream().map(RoleGrantedAuthority :: getRoleId).collect(Collectors.toSet());
			}else {
				roleIds.add(roleId);
			}
			
			if(roleIds.size() == 0) {
				return null;
			}
			result = JpaUtil
				.linq(Permission.class)
				.in("roleId",roleIds)
				//.equal("resourceType", ResourceType.URL)
				//.collect(Url.class, "resourceId")
				.list();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Override
	@SecurityCacheEvict
	@Transactional(readOnly = false)
	public void save(List<Permission> permissions) {
		JpaUtil.save(permissions);
	}
	
	@Override
	@SecurityCacheEvict
	@Transactional(readOnly = false)
	public void update(List<Permission> permissions) {
		JpaUtil.update(permissions);
	}
	
	@Override
	@SecurityCacheEvict
	@Transactional(readOnly = false)
	public void delete(List<Permission> permissions) {
		JpaUtil.delete(permissions);
	}
}
