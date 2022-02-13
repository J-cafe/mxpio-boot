package com.mxpioframework.excel.importer.parser.impl;

import org.springframework.stereotype.Component;

import com.mxpioframework.excel.importer.Constants;
import com.mxpioframework.excel.importer.model.MappingRule;
import com.mxpioframework.excel.importer.parser.CellPostParser;
import com.mxpioframework.excel.importer.policy.Context;

import net.sf.cglib.beans.BeanMap;

@Component("importer.defaultCellPostParser")
public class DefaultCellPostParser implements CellPostParser {

	@Override
	public String getName() {
		return "默认后置解析器";
	}

	@Override
	public void parse(Context context) {
		if (context.getValue() != null) {
			MappingRule mappingRule =context.getCurrentMappingRule();
			BeanMap beanMap = BeanMap.create(context.getCurrentEntity());
			if (context.getValue() != Constants.IGNORE_ERROR_FORMAT_DATA) {
				beanMap.put(mappingRule.getPropertyName(), context.getValue());
			}
		}
	}

}
