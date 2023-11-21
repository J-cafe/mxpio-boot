package com.mxpioframework.system.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	public Map<String, List<? extends DataSet>> list() {
		Map<String, List<? extends DataSet>> result = new HashMap<String, List<? extends DataSet>>();
		if(providers != null){
			for(DataSetProvider provider : providers){
				result.put(provider.getTypeName(), provider.getDataSets());
			}
		}
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public Object getData(String type, String code) {
		if(providers != null){
			for(DataSetProvider provider : providers){
				if(provider.support(type)){
					DataSet dataSet = provider.getDataSet(code);
					return provider.getResult(dataSet);
				}
			}
		}
		return null;
	}

	@Override
	public Page<?> getData(String type, String code, Pageable pageAble) {
		if(providers != null){
			for(DataSetProvider provider : providers){
				if(provider.support(type)){
					DataSet dataSet = provider.getDataSet(code);
					return provider.getPagingResult(dataSet, pageAble);
				}
			}
		}
		return null;
	}
	
}
