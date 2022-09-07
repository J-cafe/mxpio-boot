package com.mxpioframework.multitenant.listener;

import com.mxpioframework.multitenant.Constants;
import com.mxpioframework.multitenant.domain.DataSourceInfo;
import com.mxpioframework.multitenant.domain.Organization;
import com.mxpioframework.multitenant.service.DatabaseNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1000)
public class MysqlDataSourceCreateListener implements DataSourceCreateListener {

	@Autowired
	private DatabaseNameService databaseNameService;
	
	@Override
	public void onCreate(Organization organization, DataSourceInfo dataSourceInfo,
						 DataSourceBuilder<?> dataSourceBuilder) {
		if ("com.mysql.jdbc.Driver".equals(dataSourceInfo.getDriverClassName()) 
				|| "com.mysql.cj.jdbc.Driver".equals(dataSourceInfo.getDriverClassName())) {
			String url = dataSourceInfo.getUrl();
			if (!url.contains(databaseNameService.getDatabaseName(Constants.MASTER))) {
				String databaseName = databaseNameService.getDatabaseName(Constants.MASTER);
				url += "/" + databaseName;
				dataSourceBuilder.url(url);
			}
		}
		
	}

	

	

}
