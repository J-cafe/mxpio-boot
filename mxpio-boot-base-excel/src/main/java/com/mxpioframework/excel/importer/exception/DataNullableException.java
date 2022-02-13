package com.mxpioframework.excel.importer.exception;

public class DataNullableException extends DataException {
	
	private static final long serialVersionUID = 1L;

	public DataNullableException(int row, int col) {
		super(row + "行" + col + "列" + "的值不能为空");

	}

}
