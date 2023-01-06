package com.mxpioframework.multitenant.service.impl;

import com.mxpioframework.multitenant.Constants;
import com.mxpioframework.multitenant.service.DatabaseNameService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("mxpio.multitenant.databaseNameService")
public class DatabaseNameServiceImpl implements DatabaseNameService {

	@Value("${mxpio.multitenant.databaseNamePrefix:}")
	private String databaseNamePrefix;
	
	@Value("${mxpio.multitenant.databaseNameSuffix:}")
	private String databaseNameSuffix;
	
	@Value("${mxpio.multitenant.masterDatabaseName:}")
	private String masterDatabaseName;
	
	@Override
	public String getDatabaseName(String organizationId) {
		if (Constants.MASTER.equals(organizationId) && !StringUtils.isEmpty(masterDatabaseName)) {
			return masterDatabaseName;
		}
		String databaseName = organizationId;
		if (!StringUtils.isEmpty(databaseNamePrefix)) {
			databaseName = databaseNamePrefix + databaseName;
		}
		if (!StringUtils.isEmpty(databaseNameSuffix)) {
			databaseName += databaseNameSuffix;
		}
		return databaseName;
	}

}
