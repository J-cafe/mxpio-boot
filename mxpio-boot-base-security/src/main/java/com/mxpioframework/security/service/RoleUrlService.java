package com.mxpioframework.security.service;

import java.util.List;

import com.mxpioframework.security.entity.Permission;

public interface RoleUrlService {

	List<Permission> load();

	void save(List<Permission> permissions);

	void delete(List<Permission> permissions);

	void update(List<Permission> permissions);

}
