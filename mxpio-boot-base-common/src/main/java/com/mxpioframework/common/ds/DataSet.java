package com.mxpioframework.common.ds;

public interface DataSet {

	/**
	 * 数据集Code
	 * @return 数据集Code
	 */
	public String getCode();
	
	/**
	 * 数据集名称
	 * @return 数据集名称
	 */
	public String getName();
	
	/**
	 * 数据集类型
	 * @return 数据集类型
	 */
	public String getType();

}
