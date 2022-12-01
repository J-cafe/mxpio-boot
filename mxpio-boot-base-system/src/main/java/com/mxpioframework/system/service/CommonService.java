package com.mxpioframework.system.service;

public interface CommonService {

	/**
	 * 重复校验
	 * @param tableName
	 * @param column
	 * @param key
	 * @param exclude 
	 * @return
	 */
	Long duplicate(String tableName, String column, String key, String exclude);

}
