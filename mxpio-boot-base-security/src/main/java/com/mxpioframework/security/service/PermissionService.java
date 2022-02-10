package com.mxpioframework.security.service;

import java.util.Collection;
import java.util.List;

import com.mxpioframework.security.entity.Element;
import com.mxpioframework.security.entity.Permission;

public interface PermissionService {

	Collection<Element> loadElements(String pageId);

	List<Permission> loadPermissions(String roleId, String urlId);

	void save(Permission permission);
	
	public void delete(Permission permission);

}
