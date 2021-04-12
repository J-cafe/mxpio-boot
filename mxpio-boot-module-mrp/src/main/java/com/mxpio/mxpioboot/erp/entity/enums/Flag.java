package com.mxpio.mxpioboot.erp.entity.enums;

public enum Flag {
	Y("是"),
	N("否");
	
	private String text;
	
	private Flag(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
}
