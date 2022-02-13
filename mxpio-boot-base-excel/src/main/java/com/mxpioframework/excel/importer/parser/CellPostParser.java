package com.mxpioframework.excel.importer.parser;

import com.mxpioframework.excel.importer.policy.Context;

public interface CellPostParser {
	
	final String DEFAULT = "importer.defaultCellPostParser";
	
	String getName();
	
	void parse(Context context);
}
