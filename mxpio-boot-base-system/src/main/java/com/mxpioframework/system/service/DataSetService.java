package com.mxpioframework.system.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mxpioframework.common.ds.DataSet;

public interface DataSetService {

	public Map<String, List<? extends DataSet>> list();

	public Object getData(String type, String code);

	public Page<?> getData(String type, String code, Pageable pageAble);

}
