package com.mxpioframework.excel.importer.converter.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.mxpioframework.excel.importer.converter.TypeConverter;

@Component("importer.enumTypeConverter")
public class EnumTypeConverter implements TypeConverter {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object fromText(Class type, String text) {
		if(StringUtils.isEmpty(text)){
			return null;
		}
		return Enum.valueOf(type, text);
	}

	@Override
	public boolean support(Class<?> clazz) {
		if(Enum.class.isAssignableFrom(clazz)){
			return true;
		}
		return false;
	}

}