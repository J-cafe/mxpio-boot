package com.mxpioframework.dbconsole.provider;

import java.util.List;
import java.util.Map;

import com.mxpioframework.common.ds.provider.AbstractEntityDataSetProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.mxpioframework.common.ds.DataSet;
import com.mxpioframework.dbconsole.entity.DbDataSet;
import com.mxpioframework.dbconsole.service.DbDataSetService;
import com.mxpioframework.dbconsole.service.DbService;

@Component
public class DbEntityDataSetProvider extends AbstractEntityDataSetProvider {
	
	@Autowired
	private DbDataSetService dbDataSetService;
	
	@Autowired
	@Qualifier(DbService.BEAN_ID)
	private DbService dbService;

	@Override
	public boolean support(String type) {
		return getTypeKey().equals(type);
	}

	@Override
	public List<?> getResult(DataSet dataSet) {
		if(dataSet instanceof DbDataSet){
			try {
				Page<Map<String, Object>> page =  dbService.pagingSqlData(((DbDataSet) dataSet).getDbId(), ((DbDataSet) dataSet).getSqlStr(), null);
				return page.getContent();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}

	@Override
	public Page<?> getPagingResult(DataSet dataSet, Pageable page) {
		if(dataSet instanceof DbDataSet){
			try {
				Page<Map<String, Object>> result =  dbService.pagingSqlData(((DbDataSet) dataSet).getDbId(), ((DbDataSet) dataSet).getSqlStr(), page);
				return result;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	@Override
	public DataSet getDataSet(String code) {
		return dbDataSetService.getById(DbDataSet.class, code);
	}

	@Override
	public void addDataSet(DataSet dataSet) {
		if(dataSet instanceof DbDataSet){
			dbDataSetService.save((DbDataSet) dataSet);
		}
	}

	@Override
	public void deleteDataSet(String code) {
		dbDataSetService.delete(code, DbDataSet.class);
	}

	@Override
	public void updateDataSet(DataSet dataSet) {
		if(dataSet instanceof DbDataSet){
			dbDataSetService.update((DbDataSet) dataSet);
		}
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

	@Override
	public Class<DbDataSet> getClazz() {
		return DbDataSet.class;
	}
}
