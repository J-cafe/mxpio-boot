package com.mxpioframework.security.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mxpioframework.jpa.query.Criteria;

public interface BaseService<T> {

	void save(List<T> entityList);

	void update(List<T> entityList);

	void delete(List<T> entityList);
	
	void save(T entity);

	void update(T entity);

	void delete(T entity);
	
	int delete(Object key, Class<T> clazz);
	
	/**
	 * 通用查询
	 * @param clazz 领域类
	 * @param c 查询构造器
	 * @return 查询结果
	 */
	public List<T> list(Class<T> clazz, Criteria c);
	
	/**
	 * 根据ID查询
	 * @param clazz 领域类
	 * @param id ID
	 * @return 查询结果
	 */
	public <ID extends Serializable> T getById(Class<T> clazz, ID id);
	
	/**
	 * 分页查询
	 * @param clazz 领域类
	 * @param page 分页对象
	 * @param c 查询构造器
	 * @return 分页结果
	 */
	public Page<T> listPage(Class<T> clazz, Pageable page, Criteria c);
}
