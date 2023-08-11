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

	@Override
	@Transactional(readOnly = false)
	@Cacheable(cacheNames = Constants.DATA_FILTER_ROLE_CACHE_KEY, keyGenerator = Constants.KEY_GENERATOR_BEAN_NAME)
	public Map<String, List<DataFilter>> findAll() {
		List<RoleDataFilter> roleDataFilters = JpaUtil.linq(RoleDataFilter.class).list();
		List<DataFilter> dataFilters = JpaUtil.linq(DataFilter.class).list();
		Map<String, DataFilter> dataFilterMap = JpaUtil.index(dataFilters);
		Map<String, List<DataFilter>> result = new HashMap<>();
		roleDataFilters.forEach(roleDataFilter -> {
			if (!result.containsKey(roleDataFilter.getRoleId())) {
				result.put(roleDataFilter.getRoleId(), new ArrayList<DataFilter>() {
					private static final long serialVersionUID = 1L;
					{
						DataFilter dataFilter = new DataFilter();
						//拷贝对象，解决同一个DataFilter被多个角色引用时，redis缓存存放对象变为对象引用，导致取值时为空的问题
						//"$ref":"$.68bd82da\\-0575\\-4143\\-b82c\\-1a0fb5bcce2a[1]"
						BeanReflectionUtils.copyProperties(dataFilter,dataFilterMap.get(roleDataFilter.getDataFilterId()));
						add(dataFilter);
					}
				});
			} else {
				DataFilter dataFilter = new DataFilter();
				BeanReflectionUtils.copyProperties(dataFilter,dataFilterMap.get(roleDataFilter.getDataFilterId()));
				result.get(roleDataFilter.getRoleId()).add(dataFilter);
			}
		});
		return result;
	}
	
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
