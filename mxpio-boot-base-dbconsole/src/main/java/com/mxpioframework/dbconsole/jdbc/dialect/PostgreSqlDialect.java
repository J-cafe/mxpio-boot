package com.mxpioframework.dbconsole.jdbc.dialect;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.List;

@Component
public class PostgreSqlDialect extends AbstractDialect {

	public boolean support(Connection connection) {
		return support(connection, "PostgreSQL", null);
	}
	public String getPaginationSql(String sql, int pageNo, int pageSize) {
		int startNo = (pageNo - 1) * pageSize;
		return sql + " LIMIT " + pageSize + " OFFSET " + startNo;
	}

	public String getTableRenameSql(String tableName, String newTableName) {
		return " ALTER TABLE "+tableName+" RENAME TO "+newTableName;
	}


	public String getNewColumnSql(ColumnInfo dbColumnInfo) {
		String tableName=dbColumnInfo.getTableName();
		String columnName=dbColumnInfo.getColumnName();
		String columnType=dbColumnInfo.getColumnType();
		String columnSize=dbColumnInfo.getColumnSize();
		boolean isnullAble=dbColumnInfo.isIsnullAble();
		boolean isprimaryKey=dbColumnInfo.isIsprimaryKey();
		String pkName=dbColumnInfo.getPkName();
		List<String> primaryKeys=dbColumnInfo.getListPrimaryKey();
		StringBuilder sql=new StringBuilder("alter table "+tableName+" add "+columnName);	
		sql.append(this.generateColumnTypeSql(columnType, columnSize));
		sql.append(this.generateCreateDefinitionSql(isnullAble, isprimaryKey));
		if(isprimaryKey){
			if(primaryKeys.size()==1){
				sql.append(";");
	    		sql.append(this.generateAlertPrimaryKeySql(tableName, primaryKeys));
	    	}else {
	    		sql.append(";");
	    		sql.append(this.generateDropPrimaryKeySql(tableName, pkName));
	    		sql.append(";");
	    		sql.append(this.generateAlertPrimaryKeySql(tableName, primaryKeys));
	    	}
		}
		return sql.toString();
	}


	public String getUpdateColumnSql(ColumnInfo oldDbColumnInfo,ColumnInfo newDbColumnInfo) {
		String tableName=newDbColumnInfo.getTableName();
		String newColumnName=newDbColumnInfo.getColumnName();
		String oldColumnName=oldDbColumnInfo.getColumnName();
		String columnType=newDbColumnInfo.getColumnType();
		String columnSize=newDbColumnInfo.getColumnSize();
		boolean isnullAble=newDbColumnInfo.isIsnullAble();
		boolean oldNullAble=oldDbColumnInfo.isIsnullAble();
		boolean isprimaryKey=newDbColumnInfo.isIsprimaryKey();
		boolean oldPrimaryKey=oldDbColumnInfo.isIsprimaryKey();
		String pkName=newDbColumnInfo.getPkName();
		List<String> primaryKeys=newDbColumnInfo.getListPrimaryKey();
		String cType=this.generateColumnTypeSql(columnType, columnSize);
		StringBuilder sql=new StringBuilder();
	    if(!oldColumnName.toLowerCase().equals(newColumnName.toLowerCase())){
	    	sql.append(" ALTER TABLE ").append(tableName).append(" RENAME COLUMN ").append(oldColumnName).append(" to ").append(newColumnName);
	    	sql.append(";");
	    }
	    sql.append("ALTER TABLE  ").append(tableName).append(" modify   ").append(newColumnName).append(cType);
	    if(isprimaryKey!=oldPrimaryKey){
	    	if(primaryKeys.size()==1&& isprimaryKey){
	    		sql.append(";");
	    		sql.append(this.generateAlertPrimaryKeySql(tableName, primaryKeys));
	    	}else {
	    		sql.append(";");
	    		sql.append(this.generateDropPrimaryKeySql(tableName, pkName));
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
	private String generateCreateDefinitionSql(boolean isnullAble, boolean isprimaryKey){
		StringBuilder sql =new StringBuilder(" ");
		if (isnullAble) {
			sql.append("  NULL ");
		} else if(!isprimaryKey){
			sql.append(" NOT NULL ");
		}
		return sql.toString();
	}
	private String generateDropPrimaryKeySql(String tableName, String pkName){
		return "ALTER TABLE "+tableName+" DROP CONSTRAINT "+ pkName ;
	}
}


