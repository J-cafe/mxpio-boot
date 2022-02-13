package com.mxpioframework.excel.importer.converter.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.mxpioframework.excel.importer.converter.TypeConverter;

@Component("importer.booleanTypeConverter")
public class BooleanTypeConverter implements TypeConverter {

	@Override
	public Object fromText(Class<?> type, String text) {
		if (StringUtils.isBlank(text)) {
			return false;
		}
		return Boolean.parseBoolean(text);
	}

	@Override
	public boolean support(Class<?> clazz) {
		return Boolean.class.isAssignableFrom(clazz)||boolean.class.isAssignableFrom(clazz);
	}
	
}
