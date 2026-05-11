package com.mxpioframework.multitenant.service;

import javax.sql.DataSource;

import com.mxpioframework.multitenant.domain.Organization;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

public interface DataSourceService {

	DataSource getDataSource(Organization organization);
	
	DataSource createDataSource(Organization organization);
	
	DataSource getOrCreateDataSource(Organization organization);
	
	void removeDataSource(Organization organization);

	DataSource getOrCreateDataSource(String organizationId);

	void clearDataSource();

	SingleConnectionDataSource createSingleConnectionDataSource(Organization organization);
}
