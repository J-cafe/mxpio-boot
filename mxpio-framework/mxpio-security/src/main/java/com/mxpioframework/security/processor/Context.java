package com.mxpioframework.security.processor;

import java.util.List;

import com.mxpioframework.jpa.policy.impl.CrudType;

public class Context<T> {

	private List<T> entityList;
	
	private CrudType crudType;
	
	public Context(List<T> entityList, CrudType crudType) {
		this.entityList = entityList;
		this.crudType = crudType;
	}

	public List<T> getEntityList() {
		return entityList;
	}

	public void setEntityList(List<T> entityList) {
		this.entityList = entityList;
	}

	public CrudType getCrudType() {
		return crudType;
	}

	public void setCrudType(CrudType crudType) {
		this.crudType = crudType;
	}

}
