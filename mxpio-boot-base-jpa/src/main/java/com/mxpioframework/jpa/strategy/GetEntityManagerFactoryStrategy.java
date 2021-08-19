package com.mxpioframework.jpa.strategy;

import javax.persistence.EntityManagerFactory;

public interface GetEntityManagerFactoryStrategy {
	
	EntityManagerFactory getEntityManagerFactory(Class<?> domainClass);

}
