package com.mxpioframework.excel.importer.converter.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.mxpioframework.excel.importer.converter.TypeConverter;

@Component("importer.integerTypeConverter")
public class IntegerTypeConverter implements TypeConverter {

	@Override
	public Object fromText(Class<?> type, String text) {
		if (StringUtils.isBlank(text)) {
			if (Integer.class.isAssignableFrom(type)) {
				return null;
			} else {
				return 0;
			}
		}
		return Integer.parseInt(text);
	}

	@Override
	public boolean support(Class<?> clazz) {
		return int.class.isAssignableFrom(clazz)||Integer.class.isAssignableFrom(clazz);
	}
	
}