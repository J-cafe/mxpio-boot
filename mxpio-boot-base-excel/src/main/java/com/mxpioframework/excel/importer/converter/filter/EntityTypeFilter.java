package com.mxpioframework.excel.importer.converter.filter;

import java.util.List;

import jakarta.persistence.metamodel.EntityType;

public interface EntityTypeFilter {

	void filter(List<EntityType<?>> entityTypes);

}
