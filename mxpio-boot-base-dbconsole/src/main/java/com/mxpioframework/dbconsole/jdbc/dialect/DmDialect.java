package com.mxpioframework.dbconsole.jdbc.dialect;

import java.sql.Connection;
import java.util.List;

public class DmDialect extends AbstractDialect {

	public boolean support(Connection connection) {
		return support(connection, "DM DBMS", null);
	}

	public String getPaginationSql(String sql, int pageNo, int pageSize) {
		int startNo = (pageNo - 1) * pageSize;
		int endNo = pageNo * pageSize;

		sql = sql.trim();
		String forUpdateClause = null;
		boolean isForUpdate = false;
		final int forUpdateIndex = sql.toLowerCase().lastIndexOf("for update");
		if (forUpdateIndex > -1) {
			// save 'for update ...' and then remove it
			forUpdateClause = sql.substring(forUpdateIndex);
			sql = sql.substring(0, forUpdateIndex - 1);
			isForUpdate = true;
		}

		StringBuilder pagingSelect = new StringBuilder(sql.length() + 100);
		pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");
		pagingSelect.append(sql);
		pagingSelect.append(" ) row_ where rownum <= ").append(endNo).append(") where rownum_ > ").append(startNo);
		if (isForUpdate) {
			pagingSelect.append(" ");
			pagingSelect.append(forUpdateClause);
		}
		return pagingSelect.toString();
	}

	public String getTableRenameSql(String tableName, String newTableName) {
		return " alter table " + tableName + " rename to " + newTableName;
	}

	public String getNewColumnSql(ColumnInfo dbColumnInfo) {
		String tableName = dbColumnInfo.getTableName();
		String columnName = dbColumnInfo.getColumnName();
		String columnType = dbColumnInfo.getColumnType();
		String columnSize = dbColumnInfo.getColumnSize();
		boolean isnullAble = dbColumnInfo.isIsnullAble();
		boolean isprimaryKey = dbColumnInfo.isIsprimaryKey();
		List<String> primaryKeys = dbColumnInfo.getListPrimaryKey();
		StringBuilder sql = new StringBuilder("alter table " + tableName + " add " + columnName);
		sql.append(this.generateColumnTypeSql(columnType, columnSize));
		sql.append(this.generateCreateDefinitionSql(isnullAble, isprimaryKey));
		if (isprimaryKey) {
			if (primaryKeys.size() == 1) {
				sql.append(";");
				sql.append(this.generateAlertPrimaryKeySql(tableName, primaryKeys));
			} else {
				sql.append(";");
				sql.append(this.generateDropPrimaryKeySql(tableName, dbColumnInfo.getPkName()));
				sql.append(";");
				sql.append(this.generateAlertPrimaryKeySql(tableName, primaryKeys));
			}
		}
		return sql.toString();
	}

	public String getUpdateColumnSql(ColumnInfo oldDbColumnInfo, ColumnInfo newDbColumnInfo) {
		String tableName = newDbColumnInfo.getTableName();
		String newColumnName = newDbColumnInfo.getColumnName();
		String oldColumnName = oldDbColumnInfo.getColumnName();
		String columnType = newDbColumnInfo.getColumnType();
		String columnSize = newDbColumnInfo.getColumnSize();
		boolean isnullAble = newDbColumnInfo.isIsnullAble();
		boolean oldNullAble = oldDbColumnInfo.isIsnullAble();
		boolean isprimaryKey = newDbColumnInfo.isIsprimaryKey();
		boolean oldPrimaryKey = oldDbColumnInfo.isIsprimaryKey();
		List<String> primaryKeys = newDbColumnInfo.getListPrimaryKey();
		String cType = this.generateColumnTypeSql(columnType, columnSize);
		StringBuilder sql = new StringBuilder();
		if (!oldColumnName.equalsIgnoreCase(newColumnName)) {
			sql.append(" ALTER TABLE ").append(tableName).append(" RENAME COLUMN ").append(oldColumnName).append(" to ").append(newColumnName);
			sql.append(";");
		}
		sql.append("ALTER TABLE  ").append(tableName).append(" modify   ").append(newColumnName).append(cType);
		if (isprimaryKey != oldPrimaryKey) {
			if (primaryKeys.size() == 1 && isprimaryKey) {
				sql.append(";");
				sql.append(this.generateAlertPrimaryKeySql(tableName, primaryKeys));
			} else {
				sql.append(";");
				sql.append(this.generateDropPrimaryKeySql(tableName, oldDbColumnInfo.getPkName()));
				sql.append(";");
				sql.append(this.generateAlertPrimaryKeySql(tableName, primaryKeys));
			}
		}
		if (isnullAble && !oldNullAble) {
			sql.append(";");
			sql.append("ALTER TABLE  ").append(tableName).append(" modify  ").append(newColumnName).append(cType).append("  NULL ");
		} else if (!isnullAble && oldNullAble) {
			sql.append(";");
			sql.append("ALTER TABLE  ").append(tableName).append(" modify ").append(newColumnName).append(cType).append(" NOT NULL ");
		}
		return sql.toString();
	}

	private String generateCreateDefinitionSql(boolean isnullAble, boolean isprimaryKey) {
		StringBuilder sql = new StringBuilder(" ");
		if (isnullAble) {
			sql.append("  NULL ");
		} else if (!isprimaryKey) {
			sql.append(" NOT NULL ");
		}
		return sql.toString();
	}

	private String generateDropPrimaryKeySql(String tableName, String constraint) {
		return constraint != null ? "ALTER TABLE " + tableName + " DROP " + constraint : "";
	}
}
