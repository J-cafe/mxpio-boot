package com.mxpioframework.excel.importer.converter.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.mxpioframework.excel.importer.converter.TypeConverter;

@Component("importer.charTypeConverter")
public class CharTypeConverter implements TypeConverter {

	public Object fromText(Class<?> type, String text) {
		if (StringUtils.isBlank(text)) {
			if (Character.class.isAssignableFrom(type)) {
				return null;
			} else {
				return ' ';
			}
		}
		return text.charAt(0);
	}
	
	@Override
	public boolean support(Class<?> clazz) {
		return char.class.isAssignableFrom(clazz)||Character.class.isAssignableFrom(clazz);
	}

	
}