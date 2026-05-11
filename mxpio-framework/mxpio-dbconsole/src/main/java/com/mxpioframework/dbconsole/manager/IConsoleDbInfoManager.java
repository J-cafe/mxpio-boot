package com.mxpioframework.dbconsole.manager;

import java.util.List;

import com.mxpioframework.dbconsole.entity.DbInfo;

/**
 * 数据库连接配置信息维护
 * 
 */
public interface IConsoleDbInfoManager {
	public static final String BEAN_ID = "mxpio.consoleDbInfoManager";

	/**
	 *
	 * @return 数据库信息
	 * @throws Exception 失败异常
	 */
	public List<DbInfo> findDbInfos() throws Exception;

	/**
	 * 根据id查找数据库连接配置
	 * 
	 * @param id 数据库ID
	 * @return 返回DbInfo对象
	 * @throws Exception 失败异常
	 */
	public DbInfo findDbInfosById(String id) throws Exception;

	/**
	 * 添加一个数据库连接
	 * 
	 * @param dbInfo 数据库信息
	 * @throws Exception 失败异常
	 */
	public void insertDbInfo(DbInfo dbInfo) throws Exception;

	/**
	 * 更新数据库连接
	 * 
	 * @param dbInfo 数据库信息
	 * @throws Exception 失败异常
	 */
	public void updateDbInfo(DbInfo dbInfo) throws Exception;

	/**
	 * 根据id删除数据库连接
	 * 
	 * @param id 数据库ID
	 * @throws Exception 失败异常
	 */
	public void deleteDbInfoById(String id) throws Exception;

}
