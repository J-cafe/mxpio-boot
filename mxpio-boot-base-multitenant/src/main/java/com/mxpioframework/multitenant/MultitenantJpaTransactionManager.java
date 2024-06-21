package com.mxpioframework.multitenant;

import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import com.mxpioframework.multitenant.domain.Organization;
import com.mxpioframework.multitenant.service.DataSourceService;
import com.mxpioframework.multitenant.service.EntityManagerFactoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Component;

@Component
public class MultitenantJpaTransactionManager extends JpaTransactionManager {

	private static final long serialVersionUID = 1L;

	@Autowired
	private EntityManagerFactoryService entityManagerFactoryService;

	@Autowired
	private DataSourceService dataSourceService;

	@Override
	public EntityManagerFactory getEntityManagerFactory() {
		Organization organization = MultitenantUtils.peekOrganization();
		if (organization != null) {
			return entityManagerFactoryService.getOrCreateEntityManagerFactory(organization);
		}
		return super.getEntityManagerFactory();

	}

	@Override
	public DataSource getDataSource() {
		Organization organization = MultitenantUtils.peekOrganization();
		if (organization != null) {
			return dataSourceService.getOrCreateDataSource(organization);
		}
		return super.getDataSource();
	}



}
