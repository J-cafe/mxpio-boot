package com.mxpioframework.system.service;

import java.util.List;
import java.util.Map;

import com.mxpioframework.common.ds.DataSet;

public interface DataSetService {

	public Map<String, List<DataSet>> list();

	public Object getData(DataSet dataSet);

}
