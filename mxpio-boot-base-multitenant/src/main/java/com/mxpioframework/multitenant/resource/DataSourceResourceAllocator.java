package com.mxpioframework.multitenant.resource;

import com.mxpioframework.multitenant.domain.DataSourceInfo;
import com.mxpioframework.multitenant.domain.Organization;
import com.mxpioframework.multitenant.service.DataSourceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(500)
public class DataSourceResourceAllocator implements ResourceAllocator {
	
	@Autowired
	private DataSourceInfoService dataSourceInfoService;

	@Override
	public void allocate(Organization organization) {
		DataSourceInfo dataSourceInfo = dataSourceInfoService.allocate(organization);
		organization.setDataSourceInfoId(dataSourceInfo.getId());
		dataSourceInfo.setDepletionIndex(dataSourceInfo.getDepletionIndex() + 1);
	}

}
