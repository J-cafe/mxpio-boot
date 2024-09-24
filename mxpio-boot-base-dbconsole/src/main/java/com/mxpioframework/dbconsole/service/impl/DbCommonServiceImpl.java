package com.mxpioframework.dbconsole.service.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.mxpioframework.dbconsole.DbConstants;
import com.mxpioframework.dbconsole.datasource.SerializableBasicDataSource;
import com.mxpioframework.dbconsole.entity.DbInfo;
import com.mxpioframework.dbconsole.jdbc.dialect.IDialect;
import com.mxpioframework.dbconsole.manager.IConsoleDbInfoManager;
import com.mxpioframework.dbconsole.model.ColumnInfo;
import com.mxpioframework.dbconsole.model.DataGridWrapper;
import com.mxpioframework.dbconsole.model.ProcInfo;
import com.mxpioframework.dbconsole.model.TableInfo;
import com.mxpioframework.dbconsole.service.IDbCommonService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(IDbCommonService.BEAN_ID)
public class DbCommonServiceImpl implements IDbCommonService {

	@Autowired
	private Collection<IDialect> dialects;

	
	@Autowired
	private DataSource dataSource;

	@Autowired
	@Qualifier(IConsoleDbInfoManager.BEAN_ID)
	private IConsoleDbInfoManager consoleDbInfoManager;

	private final Map<String, DataSource> dataSourceMap = new ConcurrentHashMap<>();

	@Override
	public List<TableInfo> findTableInfos(String dbInfoId) throws Exception {
		List<TableInfo> tablesList = new ArrayList<>();
		DataSource ds = this.getDataSourceByDbInfoId(dbInfoId);
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = DataSourceUtils.getConnection(ds);
			DatabaseMetaData metaData = conn.getMetaData();
			String url = metaData.getURL();
			String schema = null;
			if (url.toLowerCase().contains("oracle")) {
				String value = null;//Configure.getString("mxpio.default.schema");
				if (StringUtils.hasText(value)) {
					schema = value;
				} else {
					schema = metaData.getUserName();
				}
			}
			rs = metaData.getTables(null, schema, "%", new String[] { "TABLE" });
			TableInfo tableInfo = null;
			while (rs.next()) {
				tableInfo = new TableInfo();
				tableInfo.setTableName(rs.getString("TABLE_NAME"));
				tableInfo.setDbInfoId(dbInfoId);
				tablesList.add(tableInfo);
			}
			return tablesList;
		} finally {
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeConnection(conn);
		}
	}
	
	@Override
	public List<TableInfo> findViewInfos(String dbInfoId) throws Exception {
		List<TableInfo> tablesList = new ArrayList<TableInfo>();
		DataSource ds = this.getDataSourceByDbInfoId(dbInfoId);
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = DataSourceUtils.getConnection(ds);
			DatabaseMetaData metaData = conn.getMetaData();
			String url = metaData.getURL();
			String schema = null;
			if (url.toLowerCase().contains("oracle")) {
				String value = null;//Configure.getString("mxpio.default.schema");
				if (StringUtils.hasText(value)) {
					schema = value;
				} else {
					schema = metaData.getUserName();
				}
			}
			rs = metaData.getTables(null, schema, "%", new String[] { "VIEW" });
			TableInfo tableInfo = null;
			while (rs.next()) {
				tableInfo = new TableInfo();
				tableInfo.setTableName(rs.getString("TABLE_NAME"));
				tableInfo.setDbInfoId(dbInfoId);
				tablesList.add(tableInfo);
			}
			return tablesList;
		} finally {
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeConnection(conn);
		}
	}
	
	@Override
	public List<ProcInfo> findProcInfos(String dbInfoId) throws Exception {
		List<ProcInfo> procsList = new ArrayList<>();
		DataSource ds = this.getDataSourceByDbInfoId(dbInfoId);
		Connection conn = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		try {
			conn = DataSourceUtils.getConnection(ds);
			DatabaseMetaData metaData = conn.getMetaData();
			String url = metaData.getURL();
			String schema = null;
			if (url.toLowerCase().contains("oracle")) {
				String value = null;//Configure.getString("mxpio.default.schema");
				if (StringUtils.hasText(value)) {
					schema = value;
				} else {
					schema = metaData.getUserName();
				}
			}
			rs = metaData.getProcedures(null, schema, "%");
			ProcInfo procInfo = null;
			while (rs.next()) {
				procInfo = new ProcInfo();
				procInfo.setProcName(rs.getString("PROCEDURE_NAME"));
				procInfo.setDbInfoId(dbInfoId);
				procsList.add(procInfo);
			}
			rs1 = metaData.getProcedureColumns(null, schema, "test111", "%");
			ResultSetMetaData rsmd1 = rs1.getMetaData();
			int count = rsmd1.getColumnCount();
			
			while (rs1.next()) {
				for(int i=1; i<=count; i++){
					System.out.println(rsmd1.getColumnLabel(i)+":"+rs1.getString(i));
				}
			}
			return procsList;
		} finally {
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeResultSet(rs1);
			JdbcUtils.closeConnection(conn);
		}
	}

	public List<ColumnInfo> findColumnInfos(String dbInfoId, String tableName) throws Exception {
		List<ColumnInfo> list = new ArrayList<>();
		if (StringUtils.hasText(tableName) && StringUtils.hasText(dbInfoId)) {
			list = this.findMultiColumnInfos(dbInfoId, "select * from " + tableName);
			List<String> primaryKeys = this.findTablePrimaryKeys(dbInfoId, tableName);
			for (ColumnInfo ci : list) {
				ci.setIsprimaryKey(false);
				for (String key : primaryKeys) {
					if (key.equalsIgnoreCase(ci.getColumnName())) {
						ci.setIsprimaryKey(true);
					}
				}
			}
		}
		return list;
	}

	@Override
	public DataSource createDataSource(String url, String driverClassName, String username, String password) {
		BasicDataSource ds = new SerializableBasicDataSource();
		ds.setUsername(username);
		ds.setPassword(password);
		ds.setUrl(url);
		ds.setDriverClassName(driverClassName);
		ds.setMaxIdle(2);
		return ds;
	}

	@Override
	public DataSource getDataSourceByDbInfoId(String dbInfoId) throws Exception {
		if (dbInfoId.equals(DbConstants.DEFAULTDATASOURCE)) {
			return dataSource;
		}
		DataSource ds = dataSourceMap.get(dbInfoId);
		if(ds != null){
			return ds;
		}
		DbInfo dbInfo = consoleDbInfoManager.findDbInfosById(dbInfoId);
		ds = this.createDataSource(dbInfo.getUrl(), dbInfo.getDriverClass(), dbInfo.getUsername(), dbInfo.getPassword());
		dataSourceMap.put(dbInfoId, ds);
		return ds;
	}

	@Override
	public String checkDbConnection(DbInfo dbInfo) {
		try {
			if (!StringUtils.hasText(dbInfo.getUrl())) {
				return "数据库Url不能为空！";
			}
			if (!StringUtils.hasText(dbInfo.getDriverClass())) {
				return "数据库驱动类不能为空!";
			}
			try {
				Class.forName(dbInfo.getDriverClass());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				return "[" + dbInfo.getDriverClass() + "]" + "类没有找到！";
			}
			Connection conn = null;
			try {
				conn = DriverManager.getConnection(dbInfo.getUrl(), dbInfo.getUsername(), dbInfo.getPassword());
				DatabaseMetaData metaData = conn.getMetaData();
				log.debug(String.format("connection info:[DatabaseProductName=%s,DatabaseProductVersion=%s,DatabaseMajorVersion=%s,DatabaseMinorVersion=%s,DriverName=%s]",
						metaData.getDatabaseProductName(), metaData.getDatabaseProductVersion(), metaData.getDatabaseMajorVersion(), metaData.getDatabaseMinorVersion(), metaData.getDriverName()));
			} catch (SQLException e) {
				e.printStackTrace();
				return "数据库连接失败：" + e.getMessage();
			} finally {
				JdbcUtils.closeConnection(conn);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	@Override
	public List<String> findDefaultColumnType(String dbInfoId) throws Exception {
		DataSource ds = this.getDataSourceByDbInfoId(dbInfoId);
		Connection conn = null;
		ResultSet resultSet = null;
		try {
			conn = DataSourceUtils.getConnection(ds);
			DatabaseMetaData metaData = conn.getMetaData();
			resultSet = metaData.getTypeInfo();
			List<String> list = new ArrayList<String>();
			while (resultSet.next()) {
				String typeName = resultSet.getString("TYPE_NAME").toUpperCase();
				list.add(typeName);
			}
			return list;
		} finally {
			JdbcUtils.closeResultSet(resultSet);
			JdbcUtils.closeConnection(conn);
		}
	}

	@Override
	public List<String> findTablePrimaryKeys(String dbInfoId, String tableName) throws Exception {
		List<String> primaryKeys = new ArrayList<String>();
		Connection con = null;
		ResultSet rs = null;
		DataSource ds = this.getDataSourceByDbInfoId(dbInfoId);
		try {
			con = ds.getConnection();
			DatabaseMetaData metaData = con.getMetaData();
			rs = metaData.getPrimaryKeys(null, null, tableName.toUpperCase());
			while (rs.next()) {
				primaryKeys.add(rs.getString("COLUMN_NAME").toUpperCase());
			}
			return primaryKeys;
		} finally {
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeConnection(con);
		}
	}

	@Override
	public List<ColumnInfo> findMultiColumnInfos(String dbInfoId, String sql) throws Exception {
		if (!StringUtils.hasText(sql)) {
			return null;
		}
		DataSource ds = this.getDataSourceByDbInfoId(dbInfoId);
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			conn = DataSourceUtils.getConnection(ds);
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int count = rsmd.getColumnCount();
			List<ColumnInfo> columnNames = new ArrayList<ColumnInfo>();
			ColumnInfo columnInfo = null;
			for (int i = 1; i <= count; i++) {
				columnInfo = new ColumnInfo();
				columnInfo.setColumnName(rsmd.getColumnLabel(i));
				columnInfo.setColumnType(rsmd.getColumnTypeName(i));
				columnInfo.setTableName(rsmd.getTableName(i));
				if (rsmd.getPrecision(i) > 0 && !columnInfo.getColumnType().equals("DATETIME") && !columnInfo.getColumnType().equals("TIMESTAMP") && !columnInfo.getColumnType().equals("DATE")) {
					columnInfo.setColumnSize(String.valueOf(rsmd.getPrecision(i)));
				}
				if (rsmd.getScale(i) > 0 && !columnInfo.getColumnType().equals("DATETIME") && !columnInfo.getColumnType().equals("TIMESTAMP") && !columnInfo.getColumnType().equals("DATE")) {
					columnInfo.setColumnSize(columnInfo.getColumnSize() + "," + rsmd.getScale(i));
				}
				int flagI = rsmd.isNullable(i);
				if (flagI == 0) {
					columnInfo.setIsnullAble(false);
				} else {
					columnInfo.setIsnullAble(true);
				}
				columnNames.add(columnInfo);
			}
			return columnNames;
		} finally {
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeStatement(st);
			JdbcUtils.closeConnection(conn);
		}
	}

	@Override
	public DataGridWrapper queryTableData(String dbInfoId, String tableName, String sql, int pageSize, int pageNo) throws Exception {
		DataGridWrapper dgw = new DataGridWrapper();
		if (StringUtils.hasText(dbInfoId)) {
			StringBuilder selectSql = new StringBuilder();
			StringBuilder countSql = new StringBuilder();
			if (StringUtils.hasText(tableName)) {
				selectSql.append("select * from " + tableName);
				countSql.append("select count(*) from " + tableName);
			} else if (StringUtils.hasText(sql)) {
				selectSql.append(sql.replace(";", " "));
				countSql.append("select count(*) from (" + selectSql + ") A");
			} else {
				return dgw;
			}
			DataSource ds = this.getDataSourceByDbInfoId(dbInfoId);
			JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
			IDialect dialect = this.getDBDialectByDbInfoId(jdbcTemplate);
			List<ColumnInfo> listColumns = this.findMultiColumnInfos(dbInfoId, selectSql.toString());
			List<Map<String, Object>> listData;
			if (pageSize != -1 && pageNo != -1) {
				String paginationSql = dialect.getPaginationSql(selectSql.toString(), pageNo, pageSize);
				listData = jdbcTemplate.queryForList(paginationSql);
			} else {
				listData = jdbcTemplate.queryForList(selectSql.toString());
			}
			int totalCount = jdbcTemplate.queryForObject(countSql.toString(),Integer.class);
			dgw.setColumnNames(listColumns);
			dgw.setTableData(listData);
			dgw.setTotalCount(totalCount);
			return dgw;
		}
		return null;
	}
	
	@Override
	public Page<Map<String, Object>> pagingSqlData(String dbInfoId, String sql, Pageable page) throws Exception {
		if (StringUtils.hasText(dbInfoId)) {
			StringBuilder selectSql = new StringBuilder();
			StringBuilder countSql = new StringBuilder();

			selectSql.append(sql.replace(";", " "));
			countSql.append("select count(*) from (" + selectSql + ") A");
			
			DataSource ds = this.getDataSourceByDbInfoId(dbInfoId);
			JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
			IDialect dialect = this.getDBDialectByDbInfoId(jdbcTemplate);
			
			List<Map<String, Object>> listData;
			if (page.getPageSize() != -1 && page.getPageNumber() != -1) {
				String paginationSql = dialect.getPaginationSql(selectSql.toString(), page.getPageNumber(), page.getPageSize());
				listData = jdbcTemplate.queryForList(paginationSql);
			} else {
				listData = jdbcTemplate.queryForList(selectSql.toString());
			}
			long totalCount = jdbcTemplate.queryForObject(countSql.toString(),Long.class);
			
			Page<Map<String, Object>> result = new PageImpl<Map<String, Object>>(listData, page, totalCount);
			
			return result;
		}
		return null;
	}

	@Override
	public List<ColumnInfo> querySqlColumns(String dbInfoId, String sql) throws Exception {
		if (StringUtils.hasText(dbInfoId)) {
			StringBuilder selectSql = new StringBuilder();
			selectSql.append(sql.replace(";", " "));
			return this.findMultiColumnInfos(dbInfoId, selectSql.toString());
		}
		return null;
	}
	
	protected IDialect getDialect(JdbcTemplate jdbcTemplate){
		return jdbcTemplate.execute(new ConnectionCallback<IDialect>(){
			public IDialect doInConnection(Connection connection) throws SQLException,
					DataAccessException {
				IDialect result=null;
				for(IDialect dialect : dialects){
					if(dialect.support(connection)){
						result=dialect;
						break;
					}
				}
				return result;
			}
		});
	}

	public IDialect getDBDialectByDbInfoId(JdbcTemplate jdbcTemplate) throws Exception {
		return this.getDialect(jdbcTemplate);
	}

	@Override
	public IDialect getDBDialectByDbInfoId(String dbInfoId) throws Exception {
		DataSource ds = this.getDataSourceByDbInfoId(dbInfoId);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
		return this.getDBDialectByDbInfoId(jdbcTemplate);
	}
	
	public IConsoleDbInfoManager getConsoleDbInfoManager() {
		return consoleDbInfoManager;
	}

	@Override
	public DataGridWrapper queryTableData(String dbInfoId, String sql, String tableName, int pageSize, Integer pageNo, Map<String, Object> whereMap) throws Exception {
		DataGridWrapper dgw = new DataGridWrapper();
		if (org.apache.commons.lang3.StringUtils.isNotEmpty(dbInfoId)){
			StringBuilder selectSql = new StringBuilder();
			StringBuilder countSql = new StringBuilder();
			if (org.apache.commons.lang3.StringUtils.isNotEmpty(tableName)){
				selectSql.append("select * from " + tableName + " WHERE 1=1");
				countSql.append("select count(*) from " + tableName + " WHERE 1=1");
			}else if (org.apache.commons.lang3.StringUtils.isNotEmpty(sql)){
				selectSql.append(sql.replace(";", " "));
				countSql.append("select count(*) from (" + selectSql + ") A");
			}else {
				return dgw;
			}
			// 拼接where条件
			if (!whereMap.isEmpty()){
				String where = "";
				int count = 0;
				for (Map.Entry<String, Object> entry : whereMap.entrySet()){
					// 如果不是最后一个条件，加上AND
					if (count == whereMap.size() - 2){
						where += entry.getKey() + "=\'" + entry.getValue() + "\' AND ";
					}else {
						// 如果是最后一次循环，去掉AND "
//						where += entry.getKey() + "=\'" + entry.getValue() + "";
						if (count == whereMap.size() - 1){
							where += entry.getKey() + "=\'" + entry.getValue() + "\'";
						}else {
							where += entry.getKey() + "=\'" + entry.getValue() + "\' AND ";
						}
					}
					count++;
				}
				if (selectSql.toString().contains(" WHERE ")){
					// 如果已经存在 WHERE, 则在末尾添加 AND 连接符
					selectSql.append(" AND ");
					countSql.append(" AND ");
				}else {
					// 如果不存在 WHERE, 则直接添加 WHERE 子句
					selectSql.append(" WHERE ");
					countSql.append(" WHERE ");
				}
				selectSql.append(where); // 无论是否存在 WHERE, 都直接在末尾添加 WHERE 子句
				countSql.append(where);
			}
			DataSource ds = this.getDataSourceByDbInfoId(dbInfoId);
			JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
			IDialect dialect = this.getDBDialectByDbInfoId(jdbcTemplate);
			List<ColumnInfo> listColumns = this.findMultiColumnInfos(dbInfoId, selectSql.toString());
			List<Map<String, Object>> listData;
			if (pageSize != -1 && pageNo != -1) {
				String paginationSql = dialect.getPaginationSql(selectSql.toString(), pageNo, pageSize);
				listData = jdbcTemplate.queryForList(paginationSql);
			} else {
				listData = jdbcTemplate.queryForList(selectSql.toString());
			}
			int totalCount = jdbcTemplate.queryForObject(countSql.toString(),Integer.class);
			dgw.setColumnNames(listColumns);
			dgw.setTableData(listData);
			dgw.setTotalCount(totalCount);
			return dgw;
		}
		return null;
	}
}
