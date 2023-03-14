package com.mxpioframework.dbconsole.datasource;

import java.io.Serializable;

import org.apache.commons.dbcp2.BasicDataSource;

public class SerializableBasicDataSource extends BasicDataSource implements Serializable {
	private static final long serialVersionUID = 1L;

}
