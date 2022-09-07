package com.mxpioframework.multitenant.resource;


import com.mxpioframework.multitenant.domain.Organization;

public interface ResourceAllocator {

	void allocate(Organization organization);
}
