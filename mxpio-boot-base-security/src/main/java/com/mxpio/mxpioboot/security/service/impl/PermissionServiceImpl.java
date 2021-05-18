package com.mxpio.mxpioboot.security.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mxpio.mxpioboot.jpa.JpaUtil;
import com.mxpio.mxpioboot.security.cache.SecurityCacheEvict;
import com.mxpio.mxpioboot.security.entity.Element;
import com.mxpio.mxpioboot.security.entity.Permission;
import com.mxpio.mxpioboot.security.service.PermissionService;

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
