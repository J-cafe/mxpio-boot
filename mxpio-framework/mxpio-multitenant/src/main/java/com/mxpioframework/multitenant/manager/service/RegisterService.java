package com.mxpioframework.multitenant.manager.service;

import com.mxpioframework.multitenant.domain.Organization;
import com.mxpioframework.security.entity.User;

public interface RegisterService {

	boolean isExistOrganization(String organizationId);

	boolean isExistUser(String organizationId, String username);

	void registerOrganization(User user);

	void registerUser(User user);

	Organization getOrganization(String organizationId);
}
