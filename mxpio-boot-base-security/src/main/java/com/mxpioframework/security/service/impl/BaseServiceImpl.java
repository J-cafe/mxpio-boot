package com.mxpioframework.security.service.impl;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.security.service.BaseService;

public class BaseServiceImpl<T> implements BaseService<T> {

	@Override
	@Transactional(readOnly = false)
	public void save(List<T> entityList) {
		JpaUtil.save(entityList);
	}

	@Override
	@Transactional(readOnly = false)
	public void update(List<T> entityList) {
		JpaUtil.update(entityList);
	}

	@Override
	@Transactional(readOnly = false)
	public void delete(List<T> entityList) {
		JpaUtil.delete(entityList);
	}

	@Override
	@Transactional(readOnly = false)
	public void save(T entity) {
		JpaUtil.save(entity);
	}

	@Override
	@Transactional(readOnly = false)
	public void update(T entity) {
		JpaUtil.update(entity);
	}

	@Override
	@Transactional(readOnly = false)
	public void delete(T entity) {
		JpaUtil.delete(entity);
	}

	@Override
	@Transactional(readOnly = false)
	public int delete(Object key, Class<T> clazz) {
		return JpaUtil.lind(clazz).idEqual(key).delete();
	}

}
