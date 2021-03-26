package com.mxpio.mxpioboot.jpa.policy.impl;

import javax.persistence.EntityManager;

import com.mxpio.mxpioboot.jpa.policy.CrudContext;
import com.mxpio.mxpioboot.jpa.policy.CrudPolicy;

public class SmartCrudPolicy implements CrudPolicy {

	@Override
	public void apply(CrudContext context) {
		Object entity = context.getEntity();
		EntityManager entityManager = context.getEntityManager();
		if (CrudType.SAVE.equals(context.getCrudType())) {
			entityManager.persist(entity);
		} else if(CrudType.UPDATE.equals(context.getCrudType())){
			entityManager.merge(entity);
		} else if(CrudType.DELETE.equals(context.getCrudType())) {
			entityManager.remove(entityManager.merge(entity));
		}
	}
}
