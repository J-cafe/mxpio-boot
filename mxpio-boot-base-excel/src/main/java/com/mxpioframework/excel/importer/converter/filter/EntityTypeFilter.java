package com.mxpioframework.excel.importer.converter.filter;

import java.util.List;

import javax.persistence.metamodel.EntityType;

public interface EntityTypeFilter {

	void filter(List<EntityType<?>> entityTypes);
	
}
