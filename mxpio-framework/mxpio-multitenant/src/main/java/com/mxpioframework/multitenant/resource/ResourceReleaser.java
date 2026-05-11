package com.mxpioframework.multitenant.resource;

import com.mxpioframework.multitenant.domain.Organization;

public interface ResourceReleaser {

	void release(Organization organization);
}
