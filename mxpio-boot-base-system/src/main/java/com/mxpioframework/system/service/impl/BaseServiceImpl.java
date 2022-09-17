package com.mxpioframework.system.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.mxpioframework.jpa.BaseEntity;
import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.jpa.lin.Linq;
import com.mxpioframework.jpa.policy.CrudPolicy;
import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.system.entity.TreeAble;
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
	public <ID extends Serializable> void delete(Class<T> clazz, ID id) {
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
	public <ID extends Serializable> void delete(Class<T> clazz, ID id, CrudPolicy crudPolicy) {
		T entity = JpaUtil.getOne(clazz, id);
		JpaUtil.delete(entity, crudPolicy);
	}
	
	@Override
	@Transactional(readOnly = false)
	public <ID extends Serializable> void remove(Class<T> clazz, ID id) {
		T entity = JpaUtil.getOne(clazz, id);
		JpaUtil.remove(entity);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void removeBatch(Class<T> clazz, Criteria c) {
		List<T> entities = JpaUtil.linq(clazz).where(c).list();
		JpaUtil.remove(entities);
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
	public List<T> list(Class<T> clazz, Criteria c,
			Map<Class<? extends BaseEntity>, String> subQueryEntity) {
		Linq lin = JpaUtil.linq(clazz);
		for(Map.Entry<Class<? extends BaseEntity>, String> entry : subQueryEntity.entrySet()) {
			lin.addSubQueryParser(entry.getKey(), entry.getValue());
		}
		return lin.where(c).list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<T> tree(Class<T> clazz, Criteria c) {
		List<T> result = new ArrayList<T>();
		Map<String, List<T>> childrenMap = new HashMap<String, List<T>>();
		List<T> records = JpaUtil.linq(clazz).where(c).list();
		for(T record : records){
			if(record instanceof TreeAble){
				if (childrenMap.containsKey(((TreeAble<T>) record).getCurrentNodeKey())) {
					((TreeAble<T>) record).setTreeChildren(childrenMap.get(((TreeAble<T>) record).getCurrentNodeKey()));
				} else {
					((TreeAble<T>) record).setTreeChildren(new ArrayList<T>());
					childrenMap.put(((TreeAble<T>) record).getCurrentNodeKey(), ((TreeAble<T>) record).getTreeChildren());
				}

				if (((TreeAble<T>) record).getParentNodeKey() == null) {
					result.add(record);
				} else {
					List<T> children;
					if (childrenMap.containsKey(((TreeAble<T>) record).getParentNodeKey())) {
						children = childrenMap.get(((TreeAble<T>) record).getParentNodeKey());
					} else {
						children = new ArrayList<T>();
						childrenMap.put(((TreeAble<T>) record).getParentNodeKey(), children);
					}
					children.add(record);
				}
			}
			
		}
		return result;
	}
	
	@Override
	@Transactional(readOnly = true)
	public <ID extends Serializable> T getById(Class<T> clazz, ID id) {
		return JpaUtil.getOne(clazz, id);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<T> listPage(Class<T> clazz, Pageable page, Criteria c) {
		return JpaUtil.linq(clazz).where(c).paging(page);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<T> listPage(Class<T> clazz, Pageable page, Criteria c,
			Map<Class<? extends BaseEntity>, String> subQueryEntity) {
		Linq lin = JpaUtil.linq(clazz);
		for(Map.Entry<Class<? extends BaseEntity>, String> entry : subQueryEntity.entrySet()) {
			lin.addSubQueryParser(entry.getKey(), entry.getValue());
		}
		return lin.where(c).paging(page);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<T> listPageCollect(Class<T> clazz, Pageable page, Criteria c, String otherProperty,
			Class<? extends BaseEntity> collectClazz, String property) {
		return JpaUtil.linq(clazz).collect(otherProperty, collectClazz, property).where(c).paging(page);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<T> listPageCollect(Class<T> clazz, Pageable page, Criteria c, Class<? extends BaseEntity> collectClazz,
			String property) {
		return JpaUtil.linq(clazz).collect(collectClazz, property).where(c).paging(page);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<T> listPageCollect(Class<T> clazz, Pageable page, Criteria c,
			Class<? extends BaseEntity> collectClazz) {
		return JpaUtil.linq(clazz).collect(collectClazz).where(c).paging(page);
	}

	@Override
	@Transactional(readOnly = true)
	public List<T> listCollect(Class<T> clazz, Criteria c, String otherProperty,
			Class<? extends BaseEntity> collectClazz, String property) {
		return JpaUtil.linq(clazz).collect(otherProperty, collectClazz, property).where(c).list();
	}

	@Override
	@Transactional(readOnly = true)
	public List<T> listCollect(Class<T> clazz, Criteria c, Class<? extends BaseEntity> collectClazz, String property) {
		return JpaUtil.linq(clazz).collect(collectClazz, property).where(c).list();
	}

	@Override
	@Transactional(readOnly = true)
	public List<T> listCollect(Class<T> clazz, Criteria c, Class<? extends BaseEntity> collectClazz) {
		return JpaUtil.linq(clazz).collect(collectClazz).where(c).list();
	}

}
