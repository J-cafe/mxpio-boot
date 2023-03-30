package com.mxpioframework.system.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mxpioframework.common.ds.DataSet;
import com.mxpioframework.common.ds.provider.DataSetProvider;
import com.mxpioframework.system.service.DataSetService;

@Service("mxpio.system.dataSetService")
public class DataSetServiceImpl implements DataSetService {
	
	@Autowired(required = false)
	private List<DataSetProvider> providers;

	@Override
	@Transactional(readOnly = true)
	public Map<String, List<DataSet>> list() {
		Map<String, List<DataSet>> result = new HashMap<String, List<DataSet>>();
		if(providers != null){
			for(DataSetProvider provider : providers){
				result.put(provider.getTypeName(), provider.getDataSets());
			}
		}
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public Object getData(DataSet dataSet) {
		if(providers != null){
			for(DataSetProvider provider : providers){
				if(provider.support(dataSet)){
					return provider.getResult(dataSet);
				}
			}
		}
		return null;
	}
	
}
