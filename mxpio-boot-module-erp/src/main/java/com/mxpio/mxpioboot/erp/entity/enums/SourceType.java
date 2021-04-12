package com.mxpio.mxpioboot.erp.entity.enums;

public enum SourceType {

	PURCHASE("P"),
	MANUFACTURE("M");
	
	private String name;
	
	private SourceType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
