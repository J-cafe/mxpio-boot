package com.mxpioframework.excel.importer.parser;

import com.mxpioframework.excel.importer.policy.Context;

public interface CellPostParser {
	
	String DEFAULT = "importer.defaultCellPostParser";
	
	String getName();
	
	void parse(Context context);
}
