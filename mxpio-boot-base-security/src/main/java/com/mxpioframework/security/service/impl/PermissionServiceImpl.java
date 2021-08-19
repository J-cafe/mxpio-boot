package com.mxpioframework.security.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.security.cache.SecurityCacheEvict;
import com.mxpioframework.security.entity.Element;
import com.mxpioframework.security.entity.Permission;
import com.mxpioframework.security.service.PermissionService;

@Service
@Transactional(readOnly = true)
public class PermissionServiceImpl implements PermissionService {

	@Override
	public Collection<Element> loadElements(String pageId) {
		
		return null;
	}
	
	
	@Override
	public List<Permission> loadPermissions(String roleId, String urlId) {
		return JpaUtil
				.linq(Permission.class)
				.collect(Element.class, "resourceId")
				.equal("roleId", roleId)
				.equal("resourceType", Element.RESOURCE_TYPE)
				.exists(Element.class)
					.equalProperty("id", "resourceId")
					.equal("urlId", urlId)
				.list();
	}
	
	@SecurityCacheEvict
	@Override
	@Transactional
	public void save(Permission permission) {
		if (permission.getResourceId() == null) {
			permission.setId(UUID.randomUUID().toString());
			permission.setResourceId(permission.getId());
			JpaUtil.persist(permission);
		} else {
			JpaUtil.save(permission);
		}
	}

}
