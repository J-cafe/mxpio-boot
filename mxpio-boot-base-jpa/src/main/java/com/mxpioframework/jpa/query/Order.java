package com.mxpioframework.jpa.query;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.mxpioframework.jpa.support.SerializableFunction;
import com.mxpioframework.jpa.utils.LambdaUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public class Order {

	@JsonProperty(value = "fieldName")
	private String fieldName; // 属性名
	@JsonProperty(value = "desc")
	private boolean desc;

	public Order(){

	}

	public Order(String fieldName,boolean desc){
		this.fieldName = fieldName;
		this.desc = desc;
	}

	public <T, R> Order(SerializableFunction<T, R> fieldName,boolean desc){
		this.fieldName = LambdaUtils.extractPropertyName(fieldName);
		this.desc = desc;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public <T, R> void setFieldName(SerializableFunction<T,R> func){
		this.fieldName = LambdaUtils.extractPropertyName(func);
	}

	public boolean isDesc() {
		return desc;
	}

	public void setDesc(boolean desc) {
		this.desc = desc;
	}

}
