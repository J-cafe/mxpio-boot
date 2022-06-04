package com.mxpioframework.system.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mxpioframework.jpa.BaseEntity;
import com.mxpioframework.jpa.policy.CrudPolicy;
import com.mxpioframework.jpa.query.Criteria;

public interface BaseService<T extends BaseEntity> {
	
	/**
	 * 通用智能保存（智能保存默认会处理@Transient属性）
	 * @param entity
	 * @return
	 */
	public T save(T entity);
	
	/**
	 * 通用智能保存（智能保存默认会处理@Transient属性）
	 * @param entities
	 * @return
	 */
	public Collection<T> save(Collection<T> entities);
	
	/**
	 * 通用智能保存（智能保存默认会处理@Transient属性）
	 * @param entity
	 * @param crudPolicy
	 * @return
	 */
	public T save(T entity, CrudPolicy crudPolicy);
	
	/**
	 * 通用智能保存（智能保存默认会处理@Transient属性）
	 * @param entities
	 * @param crudPolicy
	 * @return
	 */
	public Collection<T> save(Collection<T> entities, CrudPolicy crudPolicy);
	
	/**
	 * 通用新增
	 * @param entity
	 * @return
	 */
	public T persist(T entity);
	
	/**
	 * 通用智能更新（智能更新默认会处理@Transient属性）
	 * @param entity
	 * @return
	 */
	public T update(T entity);
	
	/**
	 * 通用智能更新（智能更新默认会处理@Transient属性）
	 * @param entity
	 * @param crudPolicy
	 * @return
	 */
	public T update(T entity, CrudPolicy crudPolicy);
	
	/**
	 * 通用智能更新（智能更新默认会处理@Transient属性）
	 * @param entities
	 * @return
	 */
	public Collection<T> update(Collection<T> entities);
	
	/**
	 * 通用智能更新（智能更新默认会处理@Transient属性）
	 * @param entities
	 * @param crudPolicy
	 * @return
	 */
	public Collection<T> update(Collection<T> entities, CrudPolicy crudPolicy);
	
	/**
	 * 通用更新
	 * @param entity
	 * @return
	 */
	public T merge(T entity);
	
	/**
	 * 通用智能删除By id（智能删除默认会处理@Transient属性）
	 * @param clazz
	 * @param id
	 */
	public <ID extends Serializable> void delete(Class<T> clazz, ID id);
	
	/**
	 * 通用智能删除By id（智能删除默认会处理@Transient属性）
	 * @param clazz
	 * @param id
	 * @param crudPolicy
	 */
	public <ID extends Serializable> void delete(Class<T> clazz, ID id, CrudPolicy crudPolicy);
	
	/**
	 * 通用删除
	 * @param clazz
	 * @param id
	 */
	public <ID extends Serializable> void remove(Class<T> clazz, ID id);
	
	/**
	 * 通用批量删除
	 * @param clazz
	 * @param c
	 */
	public void deleteBatch(Class<T> clazz, Criteria c);
	
	/**
	 * 通用批量删除
	 * @param clazz
	 * @param c
	 * @param crudPolicy
	 */
	public void deleteBatch(Class<T> clazz, Criteria c, CrudPolicy crudPolicy);
	
	/**
	 * 通用查询
	 * @param clazz
	 * @param c
	 * @return
	 */
	public List<T> list(Class<T> clazz, Criteria c);
	
	/**
	 * 通用树查询
	 * @param clazz
	 * @param c
	 * @return
	 */
	public List<T> tree(Class<T> clazz, Criteria c);
	
	/**
	 * 根据ID查询
	 * @param clazz
	 * @param id
	 * @return
	 */
	public <ID extends Serializable> T getById(Class<T> clazz, ID id);
	
	/**
	 * 分页查询
	 * @param clazz
	 * @param page
	 * @param c
	 * @return
	 */
	public Page<T> listPage(Class<T> clazz, Pageable page, Criteria c);

}
