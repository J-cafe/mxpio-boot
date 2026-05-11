package com.mxpioframework.excel.importer.converter.filter.impl;

import java.util.Map;

import jakarta.persistence.EntityManagerFactory;

import org.springframework.stereotype.Component;

import com.mxpioframework.excel.importer.converter.filter.EntityManagerFactoryFilter;

@Component("importer.emptyEntityManagerFactoryFilter")
public class EmptyEntityManagerFactoryFilter implements EntityManagerFactoryFilter {

	@Override
	public void filter(Map<String, EntityManagerFactory> SessionFactoryMap) {

	}

}
