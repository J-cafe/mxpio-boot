package com.mxpioframework.jpa.strategy;

import jakarta.persistence.EntityManagerFactory;

public interface GetEntityManagerFactoryStrategy {

	EntityManagerFactory getEntityManagerFactory(Class<?> domainClass);

}
