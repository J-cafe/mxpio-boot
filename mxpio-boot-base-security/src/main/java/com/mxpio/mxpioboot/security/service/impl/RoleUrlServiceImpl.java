package com.mxpio.mxpioboot.security.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mxpio.mxpioboot.jpa.JpaUtil;
import com.mxpio.mxpioboot.security.cache.SecurityCacheEvict;
import com.mxpio.mxpioboot.security.entity.Permission;
import com.mxpio.mxpioboot.security.entity.RoleGrantedAuthority;
import com.mxpio.mxpioboot.security.entity.Url;
import com.mxpio.mxpioboot.security.entity.User;
import com.mxpio.mxpioboot.security.service.RoleUrlService;

@Service
public class RoleUrlServiceImpl implements RoleUrlService {

	@Override
	@Transactional(readOnly = true)
	public List<Permission> load() {
		List<Permission> result = new ArrayList<>();
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		try{
			List<RoleGrantedAuthority> roleAuths = JpaUtil.linq(RoleGrantedAuthority.class).equal("actorId", user.getUsername()).list();
			Set<String> roleIds = new HashSet<>();
			roleIds = roleAuths.stream().map(RoleGrantedAuthority :: getRoleId).collect(Collectors.toSet());
			if(roleIds.size() == 0) {
				return null;
			}
			result = JpaUtil
				.linq(Permission.class)
				.in("roleId",roleIds)
				.equal("resourceType", Url.RESOURCE_TYPE)
				.collect(Url.class, "resourceId")
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
