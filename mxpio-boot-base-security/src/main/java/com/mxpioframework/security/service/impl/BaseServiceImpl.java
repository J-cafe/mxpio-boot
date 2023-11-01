package com.mxpioframework.security.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.jpa.query.Criteria;
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
		Object[] ids;
		if(key instanceof String){
			ids = ((String) key).split(",");
		}else{
			ids = new Object[1];
			ids[0] = key;
		}
		int result = 0;
		for(Object id : ids){
			result += JpaUtil.lind(clazz).idEqual(id).delete();
		}
		return result;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<T> list(Class<T> clazz, Criteria c) {
		return JpaUtil.linq(clazz).where(c).list();
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

}
