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
	 * @param dbInfoId 数据库ID
	 * @return 表信息
	 * @throws Exception 失败异常
	 */
	public List<TableInfo> findTableInfos(String dbInfoId) throws Exception;
	
	/**
	 * 查询所有的视图信息
	 * 
	 * @param dbInfoId 数据库ID
	 * @return 视图信息
	 * @throws Exception 失败异常
	 */
	public List<TableInfo> findViewInfos(String dbInfoId) throws Exception;
	
	/**
	 * 查询所有的存储过程信息
	 * 
	 * @param dbInfoId 数据库ID
	 * @return 存储过程信息
	 * @throws Exception 失败异常
	 */
	public List<ProcInfo> findProcInfos(String dbInfoId) throws Exception;

	/**
	 * 查询表的所有列信息
	 * 
	 * @param dbInfoId 数据库ID
	 * @param tableName 表名
	 * @return 列信息
	 * @throws Exception 失败异常
	 */
	public List<ColumnInfo> findColumnInfos(String dbInfoId, String tableName) throws Exception;

	/**
	 * 返回sql语句查询的数据列信息
	 * 
	 * @param dbInfoId 数据库ID
	 * @param sql 执行SQL
	 * @return 列信息
	 * @throws Exception 失败异常
	 */
	public List<ColumnInfo> findMultiColumnInfos(String dbInfoId, String sql) throws Exception;;

	/**
	 * 查询表的所有主键
	 * 
	 * @param dbInfoId 数据库ID
	 * @param tableName 表名
	 * @return 主键
	 * @throws Exception 失败异常
	 */
	public List<String> findTablePrimaryKeys(String dbInfoId, String tableName) throws Exception;

	/**
	 * 查询某张表的所有列和数据
	 * 
	 * @param dbInfoId 数据库ID
	 * @param tableName 表名
	 * @param sql 执行SQL
	 * @param pageSize 每页条数
	 * @param pageNo 页码
	 * @return 列和数据
	 * @throws Exception 失败异常
	 */
	public DataGridWrapper queryTableData(String dbInfoId, String tableName, String sql, int pageSize, int pageNo) throws Exception;

    List<Map<String, Object>> queryListBySql(String dbInfoId, String sql) throws Exception;

    /**
	 * 查询Sql分页数据
	 * 
	 * @param dbInfoId 数据库ID
	 * @param sql 执行SQL
	 * @param page 分页对象
	 * @return 分页数据
	 * @throws Exception 失败异常
	 */
	public Page<Map<String,Object>> pagingSqlData(String dbInfoId, String sql, Pageable page) throws Exception;
	
	/**
	 * 查询Sql字段
	 * 
	 * @param dbInfoId 数据库ID
	 * @param sql 执行SQL
	 * @return 字段信息
	 * @throws Exception 失败异常
	 */
	public List<ColumnInfo> querySqlColumns(String dbInfoId, String sql) throws Exception;

	/**
	 * 创建数据库连接池
	 * 
	 * @param url 链接
	 * @param driverClassName 驱动
	 * @param username 用户名
	 * @param password 密码
	 * @return 数据库连接池
	 */
	public DataSource createDataSource(String url, String driverClassName, String username, String password);

	/**
	 * 根据数据库id获取对应的datasource
	 * 
	 * @param dbInfoId 数据库ID
	 * @return 数据源
	 * @throws Exception 失败异常
	 */
	public DataSource getDataSourceByDbInfoId(String dbInfoId) throws Exception;


	/**
	 * 获取数据库对应的方言
	 * 
	 * @param dbInfoId 数据库ID
	 * @return 方言
	 * @throws Exception 失败异常
	 */
	public IDialect getDBDialectByDbInfoId(String dbInfoId) throws Exception;

	/**
	 * 测试连接是否成功
	 * @param dbInfo 数据库信息
	 * @return 是否成功
	 */
	public String checkDbConnection(DbInfo dbInfo);

	/**
	 * 查找默认配置的数据库类型
	 * 
	 * @param dbInfoId 数据库ID
	 * @return 返回默认配置的数据库类型的集合
	 * @throws Exception 失败异常
	 */
	public List<String> findDefaultColumnType(String dbInfoId) throws Exception;

    public DataGridWrapper queryTableData(String dbInfoId, String sql, String tableName, int pageSize, Integer pageNo, Map<String, Object> map) throws Exception;
}
