package com.mxpioframework.excel.importer.converter.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.mxpioframework.excel.importer.converter.TypeConverter;

@Component("importer.byteTypeConverter")
public class ByteTypeConverter implements TypeConverter {

	@Override
	public Object fromText(Class<?> type, String text) {
		if (StringUtils.isBlank(text)) {
			if (Byte.class.isAssignableFrom(type)) {
				return null;
			} else {
				return 0;
			}
		}
		return Byte.parseByte(text);
	}

	@Override
	public boolean support(Class<?> clazz) {
		return Byte.class.isAssignableFrom(clazz)||byte.class.isAssignableFrom(clazz);
	}
}
