package com.mxpioframework.multitenant.service;


import com.mxpioframework.multitenant.domain.DataSourceInfo;
import com.mxpioframework.multitenant.domain.Organization;

public interface DataSourceInfoService {

	DataSourceInfo get(Organization organization);
	
	DataSourceInfo allocate(Organization organization);

}
