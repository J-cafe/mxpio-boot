package com.mxpioframework.common.ds.provider;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mxpioframework.common.ds.DataSet;

public interface DataSetProvider {

	/**
	 * 支持判断
	 * @param type 数据集类型
	 * @return 是否支持
	 */
	public boolean support(String type);
	
	/**
	 * 获取数据集数据
	 * @param dataSet 数据集信息
	 * @return 数据
	 */
	public List<?> getResult(DataSet dataSet);
	
	/**
	 * 分页获取数据集数据
	 * @param dataSet 数据集信息
	 * @param page 分页插件
	 * @return 分页数据
	 */
	public Page<?> getPagingResult(DataSet dataSet, Pageable page);
	
	/**
	 * 获取数据集清单
	 * @return 数据集清单
	 */
	public List<? extends DataSet> getDataSets();
	
	/**
	 * 分页获取数据集清单
	 * @param page 分页插件
	 * @return 分页数据集清单
	 */
	public Page<? extends DataSet> pagingDataSets(Pageable page);
	
	/**
	 * 数据集类别名称
	 * @return 数据集类别名称
	 */
	public String getTypeName();
	
	/**
	 * 数据集类别
	 * @return 数据集类别
	 */
	public String getTypeKey();

	/**
	 * 根据Code获取数据集
	 * @param code 数据集Code
	 * @return 数据集
	 */
	public DataSet getDataSet(String code);

	/**
	 * 添加数据集
	 *
	 * @param dataSet 数据集
	 */
	public <T extends DataSet> void addDataSet(T dataSet);

	/**
	 * 删除数据集
	 * @param code 数据集Code
	 */
	public void deleteDataSet(String code);

	/**
	 * 修改数据集
	 *
	 * @param dataSet 数据集
	 */
	public <T extends DataSet> void updateDataSet(T dataSet);

	public <T extends DataSet> T unserialize(String json);


}
