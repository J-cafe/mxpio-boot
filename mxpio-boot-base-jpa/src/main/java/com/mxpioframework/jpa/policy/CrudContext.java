package com.mxpioframework.jpa.policy;

import jakarta.persistence.EntityManager;

import com.mxpioframework.jpa.policy.impl.CrudType;

public class CrudContext {
	private Object entity;
	private EntityManager entityManager;
	private Object parent;
	private CrudType crudType;
	private boolean saveTransient = true;

	public boolean isSaveTransient() {
		return saveTransient;
	}

	public void setSaveTransient(boolean saveTransient) {
		this.saveTransient = saveTransient;
	}

	@SuppressWarnings("unchecked")
	public <T> T getEntity() {
		return (T) entity;
	}

	public void setEntity(Object entity) {
		this.entity = entity;
	}

	@SuppressWarnings("unchecked")
	public <T> T getParent() {
		return (T) parent;
	}

	public void setParent(Object parent) {
		this.parent = parent;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public CrudType getCrudType() {
		return crudType;
	}

	public void setCrudType(CrudType crudType) {
		this.crudType = crudType;
	}
}
