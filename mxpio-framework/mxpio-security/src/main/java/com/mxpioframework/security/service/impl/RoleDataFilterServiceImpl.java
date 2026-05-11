package com.mxpioframework.security.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mxpioframework.common.util.BeanReflectionUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.security.cache.SecurityCacheEvict;
import com.mxpioframework.security.common.Constants;
import com.mxpioframework.security.entity.DataFilter;
import com.mxpioframework.security.entity.RoleDataFilter;
import com.mxpioframework.security.service.RoleDataFilterService;

@Service("mxpio.security.roleDataFilterService")
public class RoleDataFilterServiceImpl implements RoleDataFilterService {

	@SecurityCacheEvict
	@Override
	@Transactional
	public void save(RoleDataFilter roleDataFilter) {
		JpaUtil.save(roleDataFilter);
	}
	
	@SecurityCacheEvict
	@Override
	@Transactional
	public void save(List<RoleDataFilter> roleDataFilters) {
		JpaUtil.save(roleDataFilters);
	}
	
	@SecurityCacheEvict
	@Override
	@Transactional
	public void delete(RoleDataFilter roleDataFilter) {
		JpaUtil.delete(roleDataFilter);
	}

	@Override
	@Transactional(readOnly = true)
	public List<RoleDataFilter> list(Criteria criteria) {
		return JpaUtil.linq(RoleDataFilter.class).where(criteria).list();
	}

	@Override
	@Transactional(readOnly = true)
	public RoleDataFilter getById(String id) {
		return JpaUtil.linq(RoleDataFilter.class).idEqual(id).findOne();
	}

	@Override
	@Transactional(readOnly = true)
	public List<RoleDataFilter> getByRoleAndResId(String resId, String roleId) {
		return JpaUtil.linq(RoleDataFilter.class).equal("roleId", roleId).exists(DataFilter.class).equalProperty("id", "dataFilterId").equal("dataResourceId", resId).end().list();
	}

}
