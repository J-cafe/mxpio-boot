package com.mxpioframework.excel.importer.converter.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.mxpioframework.excel.importer.converter.TypeConverter;

@Component("importer.stringTypeConverter")
public class StringTypeConverter implements TypeConverter {

	@Override
	public Object fromText(Class<?> type, String text) {
		if(StringUtils.isBlank(text)){
			return null;
		}
		return text;
	}

	@Override
	public boolean support(Class<?> clazz) {
		return clazz.isAssignableFrom(String.class);
	}

}
