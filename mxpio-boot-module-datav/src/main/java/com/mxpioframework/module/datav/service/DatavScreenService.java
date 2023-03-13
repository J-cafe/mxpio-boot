package com.mxpioframework.module.datav.service;

import java.util.List;

import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.module.datav.entity.DatavScreen;

public interface DatavScreenService {

	public List<DatavScreen> list(Criteria criteria);

	public DatavScreen getById(String id);

	public void save(DatavScreen datavScreen);

	public void delete(DatavScreen datavScreen);

}
