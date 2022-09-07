package com.mxpioframework.multitenant.manager.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.multitenant.domain.DataSourceInfo;
import com.mxpioframework.multitenant.manager.service.DataSourceInfoService;

@Service("manager.dataSourceInfoService")
@Transactional(readOnly = true)
public class DataSourceInfoServiceImpl implements DataSourceInfoService {
	
	@Override
	public List<DataSourceInfo> load() {
		return JpaUtil.linq(DataSourceInfo.class).list();
	}

	@Override
	@Transactional
	public void save(List<DataSourceInfo> dataSourceInfos) {
		JpaUtil.save(dataSourceInfos);

	}

	@Override
	public boolean isExist(String dataSourceInfoId) {
		return JpaUtil.linq(DataSourceInfo.class).idEqual(dataSourceInfoId).exists();
		
	}

}
