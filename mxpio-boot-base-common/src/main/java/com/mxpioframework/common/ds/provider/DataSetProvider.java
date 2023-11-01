package com.mxpioframework.common.ds.provider;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mxpioframework.common.ds.DataSet;

public interface DataSetProvider {

	/**
	 * 支持判断
	 * @param type
	 * @return
	 */
	public boolean support(String type);
	
	/**
	 * 获取数据集数据
	 * @param dataSet
	 * @return
	 */
	public Object getResult(DataSet dataSet);
	
	/**
	 * 分页获取数据集数据
	 * @param dataSet
	 * @param page
	 * @return
	 */
	public Page<?> getPagingResult(DataSet dataSet, Pageable page);
	
	/**
	 * 获取数据集清单
	 * @return
	 */
	public List<? extends DataSet> getDataSets();
	
	/**
	 * 分页获取数据集清单
	 * @param page
	 * @return
	 */
	public Page<? extends DataSet> pagingDataSets(Pageable page);
	
	/**
	 * 数据集类别名称
	 * @return
	 */
	public String getTypeName();
	
	/**
	 * 数据集类别
	 * @return
	 */
	public String getTypeKey();

	/**
	 * 根据Code获取数据集
	 * @param code
	 * @return
	 */
	public DataSet getDataSet(String code);
}
