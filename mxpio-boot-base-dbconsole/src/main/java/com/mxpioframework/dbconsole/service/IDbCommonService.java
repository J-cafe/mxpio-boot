package com.mxpioframework.dbconsole.service;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mxpioframework.dbconsole.entity.DbInfo;
import com.mxpioframework.dbconsole.jdbc.dialect.IDialect;
import com.mxpioframework.dbconsole.model.ColumnInfo;
import com.mxpioframework.dbconsole.model.DataGridWrapper;
import com.mxpioframework.dbconsole.model.ProcInfo;
import com.mxpioframework.dbconsole.model.TableInfo;

public interface IDbCommonService {

	public static final String BEAN_ID = "mxpio.dbconsole.dbCommonService";

	/**
	 * 查询所有的表信息
	 * 
	 * @param dbInfoId
	 * @return
	 * @throws Exception
	 */
	public List<TableInfo> findTableInfos(String dbInfoId) throws Exception;
	
	/**
	 * 查询所有的视图信息
	 * 
	 * @param dbInfoId
	 * @return
	 * @throws Exception
	 */
	public List<TableInfo> findViewInfos(String dbInfoId) throws Exception;
	
	/**
	 * 查询所有的存储过程信息
	 * 
	 * @param dbInfoId
	 * @return
	 * @throws Exception
	 */
	public List<ProcInfo> findProcInfos(String dbInfoId) throws Exception;

	/**
	 * 查询表的所有列信息
	 * 
	 * @param dbInfoId
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public List<ColumnInfo> findColumnInfos(String dbInfoId, String tableName) throws Exception;

	/**
	 * 返回sql语句查询的数据列信息
	 * 
	 * @param dbInfoId
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public List<ColumnInfo> findMultiColumnInfos(String dbInfoId, String sql) throws Exception;;

	/**
	 * 查询表的所有主键
	 * 
	 * @param dbInfoId
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public List<String> findTablePrimaryKeys(String dbInfoId, String tableName) throws Exception;

	/**
	 * 查询某张表的所有列和数据
	 * 
	 * @param dbInfoId
	 * @param tableName
	 * @param sql
	 * @param pageSize
	 * @param pageNo
	 * @return
	 * @throws Exception
	 */
	public DataGridWrapper queryTableData(String dbInfoId, String tableName, String sql, int pageSize, int pageNo) throws Exception;
	
	/**
	 * 查询Sql分页数据
	 * 
	 * @param dbInfoId
	 * @param sql
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public Page<Map<String,Object>> pagingSqlData(String dbInfoId, String sql, Pageable page) throws Exception;
	
	/**
	 * 查询Sql字段
	 * 
	 * @param dbInfoId
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public List<ColumnInfo> querySqlColumns(String dbInfoId, String sql) throws Exception;

	/**
	 * 创建数据库连接池
	 * 
	 * @param url
	 * @param driverClassName
	 * @param username
	 * @param password
	 * @return
	 */
	public DataSource createDataSource(String url, String driverClassName, String username, String password);

	/**
	 * 根据数据库id获取对应的datasource
	 * 
	 * @param dbInfoId
	 * @return
	 * @throws Exception
	 */
	public DataSource getDataSourceByDbInfoId(String dbInfoId) throws Exception;


	/**
	 * 获取数据库对应的方言
	 * 
	 * @param dbInfoId
	 * @return
	 * @throws Exception
	 */
	public IDialect getDBDialectByDbInfoId(String dbInfoId) throws Exception;

	/**
	 * 测试连接是否成功
	 * @param dbInfo
	 * @return
	 */
	public String checkDbConnection(DbInfo dbInfo);

	/**
	 * 查找默认配置的数据库类型
	 * 
	 * @param dbInfoId
	 * @return 返回默认配置的数据库类型的集合
	 * @throws Exception
	 */
	public List<String> findDefaultColumnType(String dbInfoId) throws Exception;

    DataGridWrapper queryTableData(String dbInfoId, String sql, String tableName, int pageSize, Integer pageNo, Map<String, Object> map) throws Exception;
}
