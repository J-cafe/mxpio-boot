package com.mxpioframework.jpa.query;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Order {

	private String fieldName; // 属性名

	private boolean desc;

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public boolean isDesc() {
		return desc;
	}

	public void setDesc(boolean desc) {
		this.desc = desc;
	}

}
