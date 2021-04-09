package com.mxpio.mxpioboot.security.service;

import java.util.Collection;
import java.util.List;

import com.mxpio.mxpioboot.security.entity.Element;
import com.mxpio.mxpioboot.security.entity.Permission;

public interface PermissionService {

	Collection<Element> loadElements(String pageId);

	List<Permission> loadPermissions(String roleId, String urlId);

	void save(Permission permission);
}
