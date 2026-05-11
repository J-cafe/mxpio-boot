package com.mxpioframework.multitenant.service;

import com.mxpioframework.multitenant.domain.Organization;

import jakarta.persistence.EntityManagerFactory;

public interface EntityManagerFactoryService {

	EntityManagerFactory getEntityManagerFactory(Organization organization);

	EntityManagerFactory createEntityManagerFactory(Organization organization);

	EntityManagerFactory getOrCreateEntityManagerFactory(Organization organization);

	void removeEntityManagerFactory(Organization organization);

	void generateTables(Organization organization);

	EntityManagerFactory createTempEntityManagerFactory(Organization organization);

}
