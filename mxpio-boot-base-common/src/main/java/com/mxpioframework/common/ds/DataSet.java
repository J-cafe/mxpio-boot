package com.mxpioframework.common.ds;

public interface DataSet {
	
	/**
	 * 数据集ID
	 * @return
	 */
	public String getCode();
	
	/**
	 * 数据集名称
	 * @return
	 */
	public String getName();
	
	/**
	 * 数据集类型
	 * @return
	 */
	public String getType();

}
