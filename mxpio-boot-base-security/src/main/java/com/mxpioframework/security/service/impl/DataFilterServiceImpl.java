package com.mxpioframework.security.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.security.cache.SecurityCacheEvict;
import com.mxpioframework.security.entity.DataFilter;
import com.mxpioframework.security.entity.Role;
import com.mxpioframework.security.service.DataFilterService;

@Service("mxpio.security.dataFilterService")
public class DataFilterServiceImpl implements DataFilterService  {
	
	@SecurityCacheEvict
	@Override
	@Transactional
	public void save(DataFilter dataFilter) {
		JpaUtil.save(dataFilter);
	}
	
	@SecurityCacheEvict
	@Override
	@Transactional
	public void save(List<DataFilter> dataFilters) {
		JpaUtil.save(dataFilters);
	}
	
	@SecurityCacheEvict
	@Override
	@Transactional(readOnly = false)
	public void delete(DataFilter dataFilter) {
		JpaUtil.delete(dataFilter);
	}

	@Override
	@Transactional(readOnly = false)
	public DataFilter getById(String id) {
		return JpaUtil.linq(Role.class).idEqual(id).findOne();
	}

	@Override
	@Transactional(readOnly = false)
	public List<DataFilter> list(Criteria criteria) {
		return JpaUtil.linq(DataFilter.class).where(criteria).list();
	}

}
