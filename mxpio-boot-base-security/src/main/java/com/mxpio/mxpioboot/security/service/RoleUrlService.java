package com.mxpio.mxpioboot.security.service;

import java.util.List;

import com.mxpio.mxpioboot.security.entity.Permission;

public interface RoleUrlService {

	List<Permission> load(String roleId);

	void save(List<Permission> permissions);

}
