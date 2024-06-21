package com.mxpioframework.excel.importer.converter.filter;

import java.util.Map;

import jakarta.persistence.EntityManagerFactory;

public interface EntityManagerFactoryFilter {

	void filter(Map<String, EntityManagerFactory> entityManagerFactory);

}
