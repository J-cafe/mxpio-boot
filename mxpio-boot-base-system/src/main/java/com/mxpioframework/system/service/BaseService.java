package com.mxpioframework.system.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mxpioframework.jpa.BaseEntity;
import com.mxpioframework.jpa.query.Criteria;

public interface BaseService<T extends BaseEntity> {
	
	/**
	 * 通用保存
	 * @param entity
	 * @return
	 */
	public T save(T entity);
	
	/**
	 * 通用更新
	 * @param entity
	 * @return
	 */
	public T update(T entity);
	
	/**
	 * 通用删除By id
	 * @param clazz
	 * @param id
	 */
	public void delete(Class<T> clazz, String id);
	
	/**
	 * 通用批量删除
	 * @param clazz
	 * @param c
	 */
	public void deleteBatch(Class<T> clazz, Criteria c);
	
	/**
	 * 通用查询
	 * @param clazz
	 * @param c
	 * @return
	 */
	public List<T> list(Class<T> clazz, Criteria c);
	
	/**
	 * 根据ID查询
	 * @param clazz
	 * @param id
	 * @return
	 */
	public T getById(Class<T> clazz, String id);
	
	/**
	 * 分页查询
	 * @param clazz
	 * @param page
	 * @param c
	 * @return
	 */
	public Page<T> listPage(Class<T> clazz, Pageable page, Criteria c);

}
