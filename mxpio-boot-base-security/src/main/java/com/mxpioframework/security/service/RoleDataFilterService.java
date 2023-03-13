package com.mxpioframework.security.service;

import java.util.List;
import java.util.Map;

import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.security.entity.DataFilter;
import com.mxpioframework.security.entity.RoleDataFilter;

public interface RoleDataFilterService {

	public Map<String, List<DataFilter>> findAll();

	public void save(RoleDataFilter roleDataFilter);

	public void save(List<RoleDataFilter> roleDataFilters);

	public void delete(RoleDataFilter roleDataFilter);

	public List<RoleDataFilter> list(Criteria criteria);

	public RoleDataFilter getById(String id);

	public List<RoleDataFilter> getByRoleAndResId(String resId, String roleId);

}
