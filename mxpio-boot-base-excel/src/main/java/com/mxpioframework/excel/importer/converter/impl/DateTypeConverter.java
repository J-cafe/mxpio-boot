package com.mxpioframework.excel.importer.converter.impl;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.stereotype.Component;

import com.mxpioframework.excel.importer.Pattern;
import com.mxpioframework.excel.importer.converter.TypeConverter;

@Component("importer.dateTypeConverter")
public class DateTypeConverter implements TypeConverter {
	
	public static final String DEFAULT_PATTERN = Pattern.DATETIME;

    public static final String[] PARSE_PATTERNS = new String[]{
            Pattern.DATE,
            Pattern.DATETIME,
            Pattern.DATETIME_MM,
            Pattern.DATETIME_SSS,
            Pattern.SYS_DATE,
            Pattern.SYS_DATETIME,
            Pattern.SYS_DATETIME_MM,
            Pattern.SYS_DATETIME_SSS
    };

	@Override
	public Object fromText(Class<?> type, String text) {
		if (StringUtils.isBlank(text)) {
			return null;
		}
		try {
			return DateUtils.parseDate(text, PARSE_PATTERNS);
		} catch (ParseException e) {
			try {
				return DateUtil.getJavaDate(Double.valueOf(text));
			} catch (Exception e2) {
				throw new RuntimeException("［" + text + "］无法转换为日期类型。");
			}
		}
	}
	
	@Override
	public boolean support(Class<?> clazz) {
		return Date.class.isAssignableFrom(clazz);
	}
}