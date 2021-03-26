package com.mxpio.mxpioboot.jpa.policy.impl;

import javax.persistence.EntityManager;

import com.mxpio.mxpioboot.jpa.policy.CrudContext;
import com.mxpio.mxpioboot.jpa.policy.CrudPolicy;

public class SmartCrudPolicyAdapter implements CrudPolicy {

	@Override
	public void apply(CrudContext context) {
		Object entity = context.getEntity();
		EntityManager entityManager = context.getEntityManager();
		if (CrudType.SAVE.equals(context.getCrudType())) {
			if (beforeInsert(context)) {
				entityManager.persist(entity);
				afterInsert(context);
			}
		} else if(CrudType.UPDATE.equals(context.getCrudType())) {
			if (beforeUpdate(context)) {
				entityManager.merge(entity);
				afterUpdate(context);
			}
		} else if(CrudType.DELETE.equals(context.getCrudType())) {
			if (beforeDelete(context)) {
				entityManager.merge(entity);
				afterDelete(context);
			}
		}
	}
	
	public boolean beforeDelete(CrudContext context) {
		return true;
	}
	
	public void afterDelete(CrudContext context) {
		
	}
	
	public boolean beforeInsert(CrudContext context) {
		return true;
	}
	
	public void afterInsert(CrudContext context) {
		
	}
	
	public boolean beforeUpdate(CrudContext context) {
		return true;
	}
	
	public void afterUpdate(CrudContext context) {
		
	}
}
