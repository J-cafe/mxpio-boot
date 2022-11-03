package com.mxpioframework.security.access.datascope.provider;

import java.util.List;

import com.mxpioframework.jpa.query.SimpleCriterion;

public interface DataScapeProvider {
	
	List<SimpleCriterion> provide();
	
}
