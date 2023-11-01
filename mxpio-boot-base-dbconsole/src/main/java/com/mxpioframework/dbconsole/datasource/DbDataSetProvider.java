package com.mxpioframework.dbconsole.datasource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.mxpioframework.common.ds.DataSet;
import com.mxpioframework.common.ds.provider.DataSetProvider;
import com.mxpioframework.dbconsole.entity.DbDataSet;
import com.mxpioframework.dbconsole.service.DbDataSetService;

@Component
public class DbDataSetProvider implements DataSetProvider {
	
	@Autowired
	private DbDataSetService dbDataSetService;

	@Override
	public boolean support(String type) {
		return getTypeKey().equals(type);
	}

	@Override
	public Object getResult(DataSet dataSet) {
		return null;
	}

	@Override
	public Page<Object> getPagingResult(DataSet dataSet, Pageable page) {
		return null;
	}
	
	@Override
	public DataSet getDataSet(String code) {
		return dbDataSetService.getById(DbDataSet.class, code);
	}

	@Override
	public List<? extends DataSet> getDataSets() {
		return dbDataSetService.list(DbDataSet.class, null);
	}
	
	@Override
	public Page<? extends DataSet> pagingDataSets(Pageable page) {
		return dbDataSetService.listPage(DbDataSet.class, page, null);
	}

	@Override
	public String getTypeName() {
		return "数据库数据集";
	}

	@Override
	public String getTypeKey() {
		return "DB";
	}

}
