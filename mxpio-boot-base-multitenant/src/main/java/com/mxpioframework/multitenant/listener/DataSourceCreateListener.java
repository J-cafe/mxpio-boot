package com.mxpioframework.multitenant.listener;

import com.mxpioframework.multitenant.domain.DataSourceInfo;
import com.mxpioframework.multitenant.domain.Organization;
import org.springframework.boot.jdbc.DataSourceBuilder;

public interface DataSourceCreateListener {

	void onCreate(Organization organization, DataSourceInfo dataSourceInfo, DataSourceBuilder<?> dataSourceBuilder);
}
