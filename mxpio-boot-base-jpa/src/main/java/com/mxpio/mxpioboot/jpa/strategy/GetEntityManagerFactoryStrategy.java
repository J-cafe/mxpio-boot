package com.mxpio.mxpioboot.jpa.strategy;

import javax.persistence.EntityManagerFactory;

public interface GetEntityManagerFactoryStrategy {
	
	EntityManagerFactory getEntityManagerFactory(Class<?> domainClass);

}
