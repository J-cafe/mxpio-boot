package com.mxpioframework.excel.importer.converter.impl;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.mxpioframework.excel.importer.converter.TypeConverter;

@Component("importer.bigDecimalTypeConverter")
public class BigDecimalTypeConverter implements TypeConverter {

	@Override
	public Object fromText(Class<?> type, String text) {
		if (StringUtils.isBlank(text)) {
			return null;
		}
		return new BigDecimal(text.replace(",", ""));
	}

	@Override
	public boolean support(Class<?> clazz) {
		return BigDecimal.class.isAssignableFrom(clazz);
	}
}
