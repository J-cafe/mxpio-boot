package com.mxpioframework.multitenant.manager.service;

import java.util.List;

import com.mxpioframework.multitenant.domain.DataSourceInfo;

public interface DataSourceInfoService {
	
	boolean isExist(String dataSourceInfoId);

	void save(List<DataSourceInfo> dataSourceInfos);

	List<DataSourceInfo> load();
}
