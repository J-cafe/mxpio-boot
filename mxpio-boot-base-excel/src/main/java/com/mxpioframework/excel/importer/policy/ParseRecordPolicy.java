package com.mxpioframework.excel.importer.policy;

public interface ParseRecordPolicy {

	final String BEAN_ID = "importer.parseRecordPolicy";
	
	void apply(Context context) throws ClassNotFoundException;
	
}
