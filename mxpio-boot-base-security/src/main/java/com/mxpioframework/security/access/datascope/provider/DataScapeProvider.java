package com.mxpioframework.security.access.datascope.provider;

import java.util.List;

import com.mxpioframework.jpa.query.Criterion;

public interface DataScapeProvider {
	
	List<Criterion> provide();
	
}
