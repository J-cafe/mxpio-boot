package com.mxpioframework.excel.importer.converter.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.mxpioframework.excel.importer.converter.TypeConverter;

@Component("importer.floatTypeConverter")
public class FloatTypeConverter implements TypeConverter {

	@Override
	public Object fromText(Class<?> type, String text) {
		if (StringUtils.isBlank(text)) {
			if (Float.class.isAssignableFrom(type)) {
				return null;
			} else {
				return 0f;
			}
		}
		return Float.parseFloat(text);
	}

	@Override
	public boolean support(Class<?> clazz) {
		return Float.class.isAssignableFrom(clazz)||float.class.isAssignableFrom(clazz);
	}
}
