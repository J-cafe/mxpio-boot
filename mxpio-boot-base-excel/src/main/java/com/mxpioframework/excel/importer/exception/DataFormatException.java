package com.mxpioframework.excel.importer.exception;

public class DataFormatException extends DataException {

	private static final long serialVersionUID = 1L;

	public DataFormatException(int row, int col, String value) {

		super(row + "行" + col + "列" + "的值“" + value + "”格式错误！");
	}

}
