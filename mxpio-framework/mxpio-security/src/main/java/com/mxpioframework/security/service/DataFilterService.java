package com.mxpioframework.security.service;

import java.util.List;

import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.security.entity.DataFilter;

public interface DataFilterService {

	public void save(DataFilter dataFilter);

	public void save(List<DataFilter> dataFilters);

	public void delete(DataFilter dataFilter);

	public DataFilter getById(String key);

	public List<DataFilter> list(Criteria criteria);

	public List<DataFilter> getByResourceId(String resId);

}
