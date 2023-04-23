package com.mxpioframework.common.ds.provider;

import java.util.List;

import com.mxpioframework.common.ds.DataSet;

public interface DataSetProvider {

	public boolean support(DataSet dataSet);
	
	public Object getResult(DataSet dataSet);
	
	public Object getPagingResult(DataSet dataSet);
	
	public List<DataSet> getDataSets();
	
	public String getTypeName();
	
	public String getTypeKey();
}
