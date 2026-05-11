package com.mxpioframework.multitenant.service;

import com.mxpioframework.multitenant.domain.Organization;

public interface OrganizationService {

	Organization get(String id);

	void register(Organization organization);
	
	void releaseResource(Organization organization);

	void allocteResource(Organization organization);
}
