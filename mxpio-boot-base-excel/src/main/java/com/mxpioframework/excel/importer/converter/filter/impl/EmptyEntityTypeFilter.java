package com.mxpioframework.excel.importer.converter.filter.impl;

import java.util.List;

import javax.persistence.metamodel.EntityType;

import org.springframework.stereotype.Component;

import com.mxpioframework.excel.importer.converter.filter.EntityTypeFilter;

@Component("importer.emptyEntityTypeFilter")
public class EmptyEntityTypeFilter implements EntityTypeFilter {

	@Override
	public void filter(List<EntityType<?>> entityTypes) {

		
	}

}
