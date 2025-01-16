package com.mxpioframework.dbconsole.service;

import java.util.Map;

import com.mxpioframework.dbconsole.model.SqlWrapper;

public interface ISqlWrapperService {
	public static final String BEAN_ID = "mxpio.dbconsole.sqlWrapperService";

	/**
	 * 添加数据浏览器记录sql
	 *
	 * @param tableName 表名
	 * @param map 数据映射
	 * @throws Exception 如果发生错误
	 */
	public SqlWrapper getInsertTableSql(String tableName, Map<String, Object> map) throws Exception;

	/**
	 * 更新数据浏览器记录sql
	 *
	 * @param tableName 表名
	 * @param map 数据映射
	 * @param oldMap 旧数据映射
	 * @throws Exception 如果发生错误
	 */
	public SqlWrapper getUpdateTableSql(String tableName, Map<String, Object> map, Map<String, Object> oldMap) throws Exception;

	/**
	 * 删除数据浏览器记录sql
	 *
	 * @param tableName 表名
	 * @param oldMap 旧数据映射
	 * @throws Exception 如果发生错误
	 */
	public SqlWrapper getDeleteTableSql(String tableName, Map<String, Object> oldMap) throws Exception;
}
