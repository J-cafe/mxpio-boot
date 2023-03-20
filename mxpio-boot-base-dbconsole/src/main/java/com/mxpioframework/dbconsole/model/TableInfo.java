package com.mxpioframework.dbconsole.model;

import com.mxpioframework.dbconsole.entity.DbInfo;

public class TableInfo {
	private DbInfo dbInfo;
	private String dbInfoId;
	private String tableName;
	
	public String getDbInfoId() {
		return dbInfoId;
	}
	public void setDbInfoId(String dbInfoId) {
		this.dbInfoId = dbInfoId;
	}
	public DbInfo getDbInfo() {
		return dbInfo;
	}
	public void setDbInfo(DbInfo dbInfo) {
		this.dbInfo = dbInfo;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

}
