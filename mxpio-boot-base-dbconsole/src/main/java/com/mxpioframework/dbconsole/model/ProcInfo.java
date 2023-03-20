package com.mxpioframework.dbconsole.model;

import com.mxpioframework.dbconsole.entity.DbInfo;

public class ProcInfo {
	private DbInfo dbInfo;
	private String dbInfoId;
	
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
	private String procName;
	public String getProcName() {
		return procName;
	}
	public void setProcName(String procName) {
		this.procName = procName;
	}

}
