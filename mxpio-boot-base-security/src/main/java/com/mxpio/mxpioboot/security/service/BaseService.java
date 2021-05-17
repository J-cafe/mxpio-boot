package com.mxpio.mxpioboot.security.service;

import java.util.List;

public interface BaseService<T> {

	void save(List<T> entityList);

	void update(List<T> entityList);

	void delete(List<T> entityList);
	
	void save(T entity);

	void update(T entity);

	void delete(T entity);
	
	int delete(Object key, Class<T> clazz);
}
