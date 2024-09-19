package com.mxpioframework.dbconsole.jdbc.dialect;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public abstract class AbstractDialect implements IDialect{
	/**
	 * 判断是否是支持的数据库类型
	 * @param connection 数据连接
	 * @param dbProductName 数据库名称
	 * @param dbMajorVersion 数据版本号
	 * @return 返回当前方言是否支持当前数据库
	 */
	public boolean support(Connection connection,String dbProductName,String dbMajorVersion) {
		try {
			DatabaseMetaData databaseMetaData = connection.getMetaData();
			String databaseProductName = databaseMetaData
					.getDatabaseProductName();
			int databaseMajorVersion = databaseMetaData
			.getDatabaseMajorVersion();
			boolean containsMysql = StringUtils.containsIgnoreCase(
					databaseProductName,dbProductName);
			if(StringUtils.isNotEmpty(dbMajorVersion)){
				return containsMysql&&Integer.parseInt(dbMajorVersion)==databaseMajorVersion;
			}
			return containsMysql;
		} catch (SQLException e) {
			return false;
		}
	}
	public String getCreateDefaultTableSql(String tableName) {
		return " CREATE TABLE "+tableName+" (ID_ VARCHAR(50) NOT NULL, PRIMARY KEY (ID_))";
	}

	public String generateColumnTypeSql(String columnType, String columnSize) {
		StringBuilder cType = new StringBuilder(" ");
		if (StringUtils.isEmpty(columnSize)) {
			cType.append(columnType);
		} else {
			String[] cs = columnSize.split(",");
			if (cs.length == 2) {
				cType.append(columnType).append("(").append(cs[0]).append(",").append(cs[1]).append(")");
			} else if (cs.length == 1) {
				cType.append(columnType).append("(").append(cs[0]).append(")");
			}
		}
		cType.append(" ");
		return cType.toString();
	}

	public String generateAlertPrimaryKeySql(String tableName,
			List<String> primaryKeys) {
		StringBuilder sb = new StringBuilder();
		StringBuilder sql = new StringBuilder(" ");
		if (primaryKeys == null) {
			return sb.toString();
		}
		int i = 1;
		for (String s : primaryKeys) {
			if (primaryKeys.size() == i) {
				sb.append(s);
			} else {
				sb.append(s).append(",");
			}
			i++;
		}
		if (!primaryKeys.isEmpty()) {
			sql.append(" ALTER TABLE ");
			sql.append(tableName);
			sql.append(" ADD CONSTRAINT PK_");
			sql.append(tableName);
			sql.append(" PRIMARY KEY (" + sb + ")");
		}
		return sql.toString();
	}
	
}
