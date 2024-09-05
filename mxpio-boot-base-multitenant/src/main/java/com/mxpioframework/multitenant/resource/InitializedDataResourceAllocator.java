package com.mxpioframework.multitenant.resource;

import com.mxpioframework.multitenant.domain.Organization;
import com.mxpioframework.multitenant.service.DataSourceService;
import com.mxpioframework.multitenant.service.ScriptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1500)
public class InitializedDataResourceAllocator implements ResourceAllocator {
	
	@Autowired
	private DataSourceService dataSourceService;
	
	@Value("${resourceScript:}")
	private String resourceScript;
	
	@Autowired
	private ScriptService scriptService;
	
	

	@Override
	public void allocate(Organization organization) {
		scriptService.runScripts(organization.getId(), dataSourceService.getDataSource(organization), resourceScript, "multitenant");
	}
}
