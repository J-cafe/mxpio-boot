package com.mxpioframework.dbconsole.jdbc.dialect;

import org.springframework.stereotype.Component;

import java.sql.Connection;

@Component
public class SQLServer2019Dialect extends SQLServer2005Dialect {

	@Override
	public boolean support(Connection connection) {
		return support(connection, "sql server", "15");
	}
}
