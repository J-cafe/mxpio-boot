package com.mxpioframework.multitenant.service;

import javax.sql.DataSource;

public interface ScriptService {

	void runScripts(DataSource dataSource, String locations, String fallback);

	void runScripts(String organizationId, DataSource dataSource, String locations, String fallback);

}
