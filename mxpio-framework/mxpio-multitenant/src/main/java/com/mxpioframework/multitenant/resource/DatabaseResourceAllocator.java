package com.mxpioframework.multitenant.resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.mxpioframework.multitenant.domain.Organization;
import com.mxpioframework.multitenant.service.DataSourceService;
import com.mxpioframework.multitenant.service.EntityManagerFactoryService;
import com.mxpioframework.multitenant.service.ScriptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.stereotype.Component;

@Component
@Order(1000)
public class DatabaseResourceAllocator implements ResourceAllocator {
	
	@Autowired
	private EntityManagerFactoryService entityManagerFactoryService;
	
	@Autowired
	private DataSourceService dataSourceService;
	
	@Autowired
	private ScriptService scriptService;
	
	@Value("${databaseScript:}")
	private String resourceScript;
	
	
	private static final Log logger = LogFactory.getLog(DatabaseResourceAllocator.class);
	

	@Override
	public void allocate(Organization organization) {
		SingleConnectionDataSource dataSource = dataSourceService.createSingleConnectionDataSource(organization);
		try {
			scriptService.runScripts(organization.getId(), dataSource, resourceScript, "database");
		} finally {
			if (dataSource != null) {
				try {
					dataSource.destroy();
				} catch (Throwable ex) {
					logger.debug("Could not destroy DataSource", ex);
				}
			}
		}
		entityManagerFactoryService.getOrCreateEntityManagerFactory(organization);
	}

}
