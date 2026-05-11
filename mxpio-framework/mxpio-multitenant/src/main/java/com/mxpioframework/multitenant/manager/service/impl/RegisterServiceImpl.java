package com.mxpioframework.multitenant.manager.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.multitenant.MultitenantUtils;
import com.mxpioframework.multitenant.domain.Organization;
import com.mxpioframework.multitenant.manager.service.RegisterService;
import com.mxpioframework.security.entity.User;
import com.mxpioframework.security.service.UserService;

@Service
@Transactional(readOnly = true)
public class RegisterServiceImpl implements RegisterService {

	@Autowired
	private UserService userService;
	
	@Autowired
	private com.mxpioframework.multitenant.service.OrganizationService organizationService;	
	@Override
	@Transactional
	public void registerOrganization(User user) {
		Organization organization = user.getOrganization();
		organizationService.register(organization);
		MultitenantUtils.doNonQuery(organization.getId(), () -> {
			userService.create(user);
		});
	}

	@Override
	public void registerUser(User user) {
		Organization organization = user.getOrganization();
		MultitenantUtils.doNonQuery(organization.getId(), () -> {
			userService.create(user);
		});
	}
	
	@Override
	public boolean isExistOrganization(String organizationId) {
		return JpaUtil.linq(Organization.class).equal("id", organizationId).exists();
	}
	
	@Override
	public boolean isExistUser(String organizationId, String username) {
		if (!isExistOrganization(organizationId)) {
			return false;
		}
		return MultitenantUtils.doQuery(organizationId, () -> {
			return userService.findByName(username) == null;
		});
	}

	@Override
	public Organization getOrganization(String organizationId) {
		return JpaUtil.getOne(Organization.class, organizationId);
	}
	

}
