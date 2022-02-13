package com.mxpioframework.excel.importer.policy;

public interface ExcelPolicy<T extends Context> {
	
	void apply(T context) throws Exception;
	
	T createContext();
	
	boolean support(String fileName);

}
