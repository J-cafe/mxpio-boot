package com.mxpioframework.security.service;

import java.util.List;

import com.mxpioframework.security.entity.Permission;

public interface RoleUrlService {

	public List<Permission> load(String roleId);

	void save(List<Permission> permissions);

	void delete(List<Permission> permissions);

	void update(List<Permission> permissions);

}
