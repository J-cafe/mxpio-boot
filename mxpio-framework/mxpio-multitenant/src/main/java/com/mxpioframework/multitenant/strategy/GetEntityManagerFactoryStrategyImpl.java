package com.mxpioframework.multitenant.strategy;

import java.util.List;

import jakarta.persistence.EntityManagerFactory;

import com.mxpioframework.jpa.strategy.GetEntityManagerFactoryStrategy;
import com.mxpioframework.multitenant.MultitenantUtils;
import com.mxpioframework.multitenant.domain.Organization;
import com.mxpioframework.multitenant.service.EntityManagerFactoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component("multitenant.getEntityManagerFactoryStrategyImpl")
@Primary
public class GetEntityManagerFactoryStrategyImpl implements
		GetEntityManagerFactoryStrategy {

	@Autowired
	private List<EntityManagerFactory> entityManagerFactories;

	@Autowired
	private EntityManagerFactory entityManagerFactory;

	@Autowired
	private EntityManagerFactoryService entityManagerFactoryService;

	@Override
	public EntityManagerFactory getEntityManagerFactory(Class<?> domainClass) {
		RuntimeException exception = new RuntimeException("entityManagerFactories can not be empty!");
		Organization organization = MultitenantUtils.peekOrganization();
		if (organization != null) {
			EntityManagerFactory entityManagerFactory = entityManagerFactoryService.getOrCreateEntityManagerFactory(organization);
			try {
				if (domainClass == null) {
					return entityManagerFactory;
				} else {
					entityManagerFactory.getMetamodel().entity(domainClass);
					return entityManagerFactory;
				}
			} catch (IllegalArgumentException e) {
				exception = e;
			}
		}

		if (domainClass == null) {
			return entityManagerFactory;
		}

		for (EntityManagerFactory emf : entityManagerFactories) {
			try {
				emf.getMetamodel().entity(domainClass);
				return emf;
			} catch (IllegalArgumentException e) {
				exception = e;
			}
		}
		throw exception;
	}

}
