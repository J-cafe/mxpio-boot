package com.mxpioframework.system.service.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.mxpioframework.jpa.BaseEntity;
import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.jpa.policy.CrudPolicy;
import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.system.service.BaseService;

public class BaseServiceImpl<T extends BaseEntity> implements BaseService<T> {

	@Override
	@Transactional(readOnly = false)
	public T save(T entity) {
		JpaUtil.save(entity);
		return entity;
	}

	@Override
	@Transactional(readOnly = false)
	public Collection<T> save(Collection<T> entities) {
		JpaUtil.save(entities);
		return entities;
	}
	
	@Override
	@Transactional(readOnly = false)
	public T save(T entity, CrudPolicy crudPolicy) {
		JpaUtil.save(entity, crudPolicy);
		return entity;
	}
	
	@Override
	@Transactional(readOnly = false)
	public Collection<T> save(Collection<T> entities, CrudPolicy crudPolicy) {
		JpaUtil.save(entities, crudPolicy);
		return entities;
	}
	
	@Override
	@Transactional(readOnly = false)
	public T persist(T entity) {
		JpaUtil.persist(entity);
		return entity;
	}
	
	@Override
	@Transactional(readOnly = false)
	public T update(T entity) {
		JpaUtil.update(entity);
		return entity;
	}
	
	@Override
	@Transactional(readOnly = false)
	public T update(T entity, CrudPolicy crudPolicy) {
		JpaUtil.update(entity, crudPolicy);
		return entity;
	}
	
	@Override
	@Transactional(readOnly = false)
	public Collection<T> update(Collection<T> entities) {
		JpaUtil.update(entities);
		return entities;
	}

	@Override
	@Transactional(readOnly = false)
	public Collection<T> update(Collection<T> entities, CrudPolicy crudPolicy) {
		JpaUtil.update(entities, crudPolicy);
		return entities;
	}
	
	@Override
	@Transactional(readOnly = false)
	public T merge(T entity) {
		JpaUtil.merge(entity);
		return entity;
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(Class<T> clazz, String id) {
		T entity = JpaUtil.getOne(clazz, id);
		JpaUtil.delete(entity);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void deleteBatch(Class<T> clazz, Criteria c) {
		List<T> entities = JpaUtil.linq(clazz).where(c).list();
		JpaUtil.delete(entities);
	}

	@Override
	@Transactional(readOnly = false)
	public void delete(Class<T> clazz, String id, CrudPolicy crudPolicy) {
		T entity = JpaUtil.getOne(clazz, id);
		JpaUtil.delete(entity, crudPolicy);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void remove(Class<T> clazz, String id) {
		T entity = JpaUtil.getOne(clazz, id);
		JpaUtil.remove(entity);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void deleteBatch(Class<T> clazz, Criteria c, CrudPolicy crudPolicy) {
		List<T> entities = JpaUtil.linq(clazz).where(c).list();
		JpaUtil.delete(entities, crudPolicy);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<T> list(Class<T> clazz, Criteria c) {
		return JpaUtil.linq(clazz).where(c).list();
	}
	
	@Override
	@Transactional(readOnly = true)
	public T getById(Class<T> clazz, String id) {
		JpaUtil.linq(clazz).idEqual(id).findOne();
		return JpaUtil.linq(clazz).idEqual(id).findOne();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<T> listPage(Class<T> clazz, Pageable page, Criteria c) {
		return JpaUtil.linq(clazz).where(c).paging(page);
	}

}
