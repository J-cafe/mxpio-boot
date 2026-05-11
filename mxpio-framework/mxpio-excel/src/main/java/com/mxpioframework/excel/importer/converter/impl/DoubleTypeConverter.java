package com.mxpioframework.excel.importer.converter.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.mxpioframework.excel.importer.converter.TypeConverter;

@Component("importer.doubleTypeConverter")
public class DoubleTypeConverter implements TypeConverter {

	@Override
	public Object fromText(Class<?> type, String text) {
		if (StringUtils.isBlank(text)) {
			if (Double.class.isAssignableFrom(type)) {
				return null;
			} else {
				return 0f;
			}
		}
		return Double.parseDouble(text);
	}

	@Override
	public boolean support(Class<?> clazz) {
		return Double.class.isAssignableFrom(clazz)||double.class.isAssignableFrom(clazz);
	}
	
}
