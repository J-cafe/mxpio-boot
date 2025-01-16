package com.mxpioframework.excel.importer.parser;

import com.mxpioframework.excel.importer.policy.Context;

public interface CellPreParser {
	
	String DEFAULT = "importer.defaultCellPreParser";

	
	String getName();
	
	void parse(Context context);
}
