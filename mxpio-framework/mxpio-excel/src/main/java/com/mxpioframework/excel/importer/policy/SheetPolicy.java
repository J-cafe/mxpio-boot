package com.mxpioframework.excel.importer.policy;

public interface SheetPolicy<T extends Context> {
	
	void apply(T context) throws Exception;
	
}