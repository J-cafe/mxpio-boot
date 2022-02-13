package com.mxpioframework.excel.importer.converter.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.mxpioframework.excel.importer.converter.TypeConverter;

@Component("importer.shortTypeConverter")
public class ShortTypeConverter implements TypeConverter {

	@Override
	public Object fromText(Class<?> type, String text) {
		if (StringUtils.isBlank(text)) {
			if (Short.class.isAssignableFrom(type)) {
				return null;
			} else {
				return 0;
			}
		}
		return Short.parseShort(text);
	}

	@Override
	public boolean support(Class<?> clazz) {
		return Short.class.isAssignableFrom(clazz)||short.class.isAssignableFrom(clazz);
	}

}
