package com.mxpioframework.security.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.mxpioframework.security.entity.Role;

public interface RoleAssignmentService {

	void loadUsersWithout(Pageable page, String roleId);

	void loadUsersWithin(Pageable page, String roleId);

	void save(List<Role> roles);
}
